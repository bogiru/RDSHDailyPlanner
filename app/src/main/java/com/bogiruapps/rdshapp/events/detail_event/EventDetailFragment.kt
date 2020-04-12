package com.bogiruapps.rdshapp.events.detail_event

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
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

    private fun showSnackBar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupObserverViewModel() {
        /*showProgress()*/
        eventDetailViewModel.openTaskEventRecyclerView.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_eventDetailFragment_to_tasksEventFragment)
        })

        eventDetailViewModel.openEventFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventsFragment)
        })

        eventDetailViewModel.openEventEditFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventEditFragment)
        })

        eventDetailViewModel.openEventDeleteFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showAllertDialogDelete()
        })

        eventDetailViewModel.showToast.observe(viewLifecycleOwner, EventObserver {
            showSnackBar(it)
        })

     /*   eventDetailViewModel.openEventDeleteFragmentEvent.observe(this, Observer {
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventsFragment)
        })*/
    }

    private fun showAllertDialogDelete(){
        val alertBuilder = MaterialAlertDialogBuilder(activity, R.style.AlertDialogTheme)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            eventDetailViewModel.deleteEvent()
            findNavController().navigate(R.id.action_eventDetailFragment_to_eventsFragment)
        }
        alertBuilder.setNegativeButton(
            "Нет") { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.event_detail_fragment, container, false)
        binding.viewModel = eventDetailViewModel
        if (eventDetailViewModel.event.countTask == 0) {
            //binding.progressBar.setDonut_progress("0")
        } else {
           // binding.progressBar.setDonut_progress("${eventDetailViewModel.event.countCompletedTask * 100 / eventDetailViewModel.event.countTask}")
        }
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        editItem?.setOnMenuItemClickListener {
            eventDetailViewModel.showEditEventFragment()
            return@setOnMenuItemClickListener true
        }

        deleteItem?.setOnMenuItemClickListener {
            eventDetailViewModel.showDeleteEventFragment()
            return@setOnMenuItemClickListener true
        }

        activity?.bottomNavigationView?.visibility = View.VISIBLE
        editItem?.isVisible = true
        deleteItem?.isVisible = true
        //Glide.with(this).load(R.drawable.header2).into(image)
        activity?.collapseToolbar?.title = ""
        //activity?.appBar?.setExpanded(true)

    }
}
