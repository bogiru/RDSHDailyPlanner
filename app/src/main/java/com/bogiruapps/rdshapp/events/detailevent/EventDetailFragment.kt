package com.bogiruapps.rdshapp.events.detailevent

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver
import org.koin.android.viewmodel.ext.android.viewModel
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.EventDetailFragmentBinding
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class  EventDetailFragment : Fragment() {

    private val eventDetailViewModel: EventDetailViewModel by viewModel()

    private lateinit var binding: EventDetailFragmentBinding

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
        eventDetailViewModel.openTaskEventFragment.observe(viewLifecycleOwner, EventObserver {
            openTaskEventFragment()
        })

        eventDetailViewModel.openEventFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            openEventFragment()
        })

        eventDetailViewModel.openEventEditFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            openEventEditFragment()
        })

        eventDetailViewModel.openDialogDeleteEvent.observe(viewLifecycleOwner, EventObserver {
            showAlertDialogDeleteSchoolEvent()
        })

        eventDetailViewModel.showSnackbar.observe(viewLifecycleOwner, EventObserver { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.event_detail_fragment, container, false)
        binding.viewModel = eventDetailViewModel
        setSchoolEventProgressBar(eventDetailViewModel.schoolEvent)
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
            eventDetailViewModel.showEditEventFragment()
            return@setOnMenuItemClickListener true
        }

        deleteItem?.setOnMenuItemClickListener {
            eventDetailViewModel.showDialogDeleteSchoolEvent()
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
                eventDetailViewModel.deleteSchoolEvent()
                openEventFragment()
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

    private fun openTaskEventFragment() {
        findNavController().navigate(R.id.action_eventDetailFragment_to_tasksEventFragment)
    }

    private fun openEventFragment() {
        findNavController().navigate(R.id.action_eventDetailFragment_to_eventsFragment)
    }

    private fun openEventEditFragment() {
        findNavController().navigate(R.id.action_eventDetailFragment_to_eventEditFragment)
    }

}
