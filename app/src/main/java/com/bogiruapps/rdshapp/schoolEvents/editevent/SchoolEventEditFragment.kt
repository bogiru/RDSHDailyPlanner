package com.bogiruapps.rdshapp.schoolEvents.editevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventEditBinding
import com.bogiruapps.rdshapp.utils.hideBottomNavigationView
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class SchoolEventEditFragment : Fragment() {

    private val schoolEventEditViewModel: SchoolEventEditViewModel by viewModel()
    private lateinit var binding: FragmentEventEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        configureToolbar()
        hideBottomNavigationView(activity!!)
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit, container, false)
        binding.viewModel = schoolEventEditViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        schoolEventEditViewModel.openSchoolEventFragment.observe(viewLifecycleOwner, EventObserver {
            openSchoolEventFragment()
        })

        schoolEventEditViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })

        schoolEventEditViewModel.showDatePickerDialog.observe(viewLifecycleOwner, Observer {
            showDatePickerDialog()
        })

        schoolEventEditViewModel.indexImage.observe(viewLifecycleOwner, Observer {
            loadImage(it)
        })
}

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false

        if (schoolEventEditViewModel.checkCreateEventStatus()) {
            activity?.main_toolbar?.title = "Создание"
        } else  {
            activity?.main_toolbar?.title = "Редактирование"
        }
    }

    private fun loadImage(indexImage: Int) {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("backgroundEvents/$indexImage.png")
        storageReference.downloadUrl.addOnCompleteListener {
            /*try {*/
                Glide.with(binding.eventEditImage).load(it.result).error(R.drawable.noavatar).into(binding.eventEditImage)
            /*} catch (e: RuntimeExecutionException) {
                Glide.with(binding.eventEditImage).load(R.drawable.noavatar).into(binding.eventEditImage)
            }*/
        }
    }

    private fun showDatePickerDialog() {
        /*val datePicker = DatePickerDialog(activity!!)
        datePicker.show()

        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            eventEditViewModel.updateDate(year - 1900, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
            binding.eventEditDeadlineTextView.text = dateFormat.format(eventEditViewModel.event!!.deadline)
        }*/
    }

    private fun openSchoolEventFragment() {
        findNavController().navigate(R.id.action_eventEditFragment_to_eventsFragment)
    }
}
