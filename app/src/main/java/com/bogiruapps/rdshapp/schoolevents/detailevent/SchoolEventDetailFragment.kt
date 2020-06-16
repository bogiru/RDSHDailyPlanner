package com.bogiruapps.rdshapp.schoolevents.detailevent

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import org.koin.android.viewmodel.ext.android.viewModel
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentSchoolEventDetailBinding
import com.bogiruapps.rdshapp.schoolevents.SchoolEvent
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class  SchoolEventDetailFragment : Fragment() {

    private val schoolEventDetailViewModel: SchoolEventDetailViewModel by viewModel()

    private lateinit var binding: FragmentSchoolEventDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        configureToolbar()
        configureBottomNavigation()
        setupObserverViewModel()

        return binding.root
    }

    private fun setupObserverViewModel() {

        schoolEventDetailViewModel.openSchoolEventEditFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            openSchoolEventEditFragment()
        })

        schoolEventDetailViewModel.openDialogDeleteEvent.observe(viewLifecycleOwner, EventObserver {
            showAlertDialogDeleteSchoolEvent()
        })

        schoolEventDetailViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_event_detail, container, false)
        binding.viewModel = schoolEventDetailViewModel
        setSchoolEventProgressBar(schoolEventDetailViewModel.schoolEvent)
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setSchoolEventProgressBar(schoolEvent: SchoolEvent) {
        val progress =
            if (schoolEvent.countTask == 0) 0
            else schoolEvent.countCompletedTask * 100 / schoolEvent.countTask

        binding.eventProgressBar.setDonut_progress("$progress")
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = true
        deleteItem?.isVisible = true

        editItem?.setOnMenuItemClickListener {
            schoolEventDetailViewModel.openSchoolEventEditFragment()
            return@setOnMenuItemClickListener true
        }

        deleteItem?.setOnMenuItemClickListener {
            schoolEventDetailViewModel.showDialogDeleteSchoolEvent()
            return@setOnMenuItemClickListener true
        }
    }

    private fun showAlertDialogDeleteSchoolEvent(){
        val alertBuilder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
        alertBuilder.apply {
            setTitle("Удалить объявление")
            setMessage("Вы уверены, что хотите удалить объявление?")
            setCancelable(true)
            setPositiveButton(
                "Да"
            ) { _: DialogInterface, _: Int ->
                schoolEventDetailViewModel.deleteSchoolEvent()
                openSchoolEventFragment()
            }
            setNegativeButton(
                "Нет") { _: DialogInterface, _: Int ->
            }
            show()
        }
    }

    private fun configureBottomNavigation() {
        activity!!.bottomNavigationView.apply {
            menu.clear()
            inflateMenu(R.menu.event_bottom_menu)
            visibility = View.VISIBLE
        }
    }

    private fun openTaskSchoolEventFragment() {
        findNavController().navigate(R.id.action_schoolEventDetailFragment_to_taskSchoolEventFragment)
    }

    private fun openSchoolEventFragment() {
        findNavController().navigate(R.id.action_schoolEventDetailFragment_to_schoolEventsFragment)
    }

    private fun openSchoolEventEditFragment() {
        findNavController().navigate(R.id.action_schoolEventDetailFragment_to_schoolEventEditFragment)
    }

}
