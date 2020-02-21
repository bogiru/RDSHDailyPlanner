package com.bogiruapps.rdshapp.events.detail_event

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.activity_main.*

class EventDetailFragment : Fragment() {

    companion object {
        fun newInstance() = EventDetailFragment()
    }

    private val eventDetailViewModel: EventDetailViewModel by viewModel()

    private lateinit var binding: EventDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        configureToolbar()
        setupObserverViewModel()

        return binding.root
    }

    private fun setupObserverViewModel() {
        /*showProgress()*/
        eventDetailViewModel.openTaskEventRecyclerView.observe(this, EventObserver {
            findNavController().navigate(R.id.action_eventDetailFragment_to_tasksEventFragment)
        })

     /*   eventDetailViewModel.openEventDeleteFragmentEvent.observe(this, Observer {
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventsFragment)
        })*/
    }

    @SuppressLint("ResourceType")
    private fun showAllertDialogDelete(){
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            eventDetailViewModel.deleteEvent()
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventsFragment)
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.event_detail_fragment, container, false)
        binding.viewModel = eventDetailViewModel
        if (eventDetailViewModel.event.countTask == 0) {
            binding.progressBar.progress = 0
        } else {
            binding.progressBar.progress = eventDetailViewModel.event.countCompletedTask * 100 / eventDetailViewModel.event.countTask
        }
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.toolbar?.menu?.findItem(R.id.item_share)?.isVisible = false
        editItem?.isVisible = true
        deleteItem?.isVisible = true

        editItem?.setOnMenuItemClickListener {
            eventDetailViewModel.setStateEdit()
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventEditFragment)
            return@setOnMenuItemClickListener true
        }

        deleteItem?.setOnMenuItemClickListener {
            showAllertDialogDelete()
            return@setOnMenuItemClickListener true
        }

    }

}
