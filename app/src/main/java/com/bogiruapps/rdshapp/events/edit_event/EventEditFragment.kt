package com.bogiruapps.rdshapp.events.edit_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventEditBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class EventEditFragment : Fragment() {

    private val eventEditViewModel: EventEditViewModel by viewModel()
    private lateinit var binding: FragmentEventEditBinding



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        setClickListenerOnCalendarView()
        configureToolbar()
        configureBottomNavigation()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit, container, false)
        binding.viewModel = eventEditViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupObserverViewModel() {
        eventEditViewModel.openSchoolEventFragment.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_eventEditFragment_to_eventsFragment)
        })

        eventEditViewModel.showSnackbar.observe(viewLifecycleOwner, Observer {
            showSnackbar(it)
        })

        eventEditViewModel.showDatePickerDialog.observe(viewLifecycleOwner, Observer {
            showDatePickerDialog()
        })

        eventEditViewModel.indexImage.observe(viewLifecycleOwner, Observer {
            loadImage(it)
        })
}

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)

        if (eventEditViewModel.checkCreateEventStatus()) {
            activity?.main_toolbar?.title = "Создание"
        } else  {
            activity?.main_toolbar?.title = "Редактирование"
        }
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureBottomNavigation() {
        activity!!.bottomNavigationView.visibility = View.GONE
    }

    private fun setClickListenerOnCalendarView() {
       /* binding.calendarViewEvent.setOnDateChangeListener { view, year, month, dayOfMonth ->

        }*/
    }

    private fun loadImage(indexImage: Int) {
        val storageReference =
            FirebaseStorage.getInstance().reference.child("backgroundEvents/$indexImage.png")
        storageReference.downloadUrl.addOnCompleteListener {
            try {
                Glide.with(binding.eventEditImage).load(it.result).error(R.drawable.noavatar).into(binding.eventEditImage)
            } catch (e: RuntimeExecutionException) {
                Glide.with(binding.eventEditImage).load(R.drawable.noavatar).into(binding.eventEditImage)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(activity!!)
        datePicker.show()

        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            eventEditViewModel.updateDate(year - 1900, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
            binding.eventEditDeadlineTextView.text = dateFormat.format(eventEditViewModel.event!!.deadline)

        }
    }

}
