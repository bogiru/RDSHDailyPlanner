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
}

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        if (eventEditViewModel.checkCreateEventStatus()) {
            activity?.toolbar?.title = "Создание мероприятия"
        } else  {
            activity?.toolbar?.title = "Редактирование мероприятия"
        }
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun setClickListenerOnCalendarView() {
        binding.calendarViewEvent.setOnDateChangeListener { view, year, month, dayOfMonth ->
            eventEditViewModel.updateDate(year - 1900, month, dayOfMonth)
        }
    }
}
