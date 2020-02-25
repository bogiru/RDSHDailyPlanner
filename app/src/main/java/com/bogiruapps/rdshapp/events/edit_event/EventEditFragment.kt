package com.bogiruapps.rdshapp.events.edit_event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventEditBinding
import com.bogiruapps.rdshapp.events.edit_event.EventEditViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class EventEditFragment : Fragment() {

    private val eventEditViewModel: EventEditViewModel by viewModel()
    private lateinit var binding: FragmentEventEditBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        setClickListenerOnCalendarView()
        configureToolbar()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit, container, false)
        binding.viewModel = eventEditViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        eventEditViewModel.openSchoolEventFragment.observe(this, EventObserver {
        findNavController().navigate(R.id.action_eventEditFragment_to_eventsFragment)
    })

}

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.collapseToolbar?.title = "Редактирование"
        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun setClickListenerOnCalendarView() {
        binding.calendarViewEvent.setOnDateChangeListener { view, year, month, dayOfMonth ->
            eventEditViewModel.updateDate(year - 1900, month, dayOfMonth)
        }
    }
}
