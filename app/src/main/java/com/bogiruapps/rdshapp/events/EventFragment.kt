package com.bogiruapps.rdshapp.events


import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventsBinding
import com.bogiruapps.rdshapp.notice.Notice
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notice.*
import org.koin.android.viewmodel.ext.android.viewModel

class EventsFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModel()

    private lateinit var adapter: EventsAdapter
    private lateinit var binding: FragmentEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        configureToolbar()

        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false)
        binding.viewModel = eventViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun setupObserverViewModel() {
        eventViewModel.openTaskEventFragment.observe(this, EventObserver {
            findNavController().navigate(R.id.action_eventsFragment_to_eventDetailFragment)
        })

        eventViewModel.openEditEventFragment.observe(this, EventObserver {
            findNavController().navigate(R.id.action_eventsFragment_to_eventEditFragment)
        })

        eventViewModel.showSchoolEventContent.observe(this, EventObserver {
            configureRecyclerView()
        })

        eventViewModel.dataLoading.observe(this, Observer { isDataLoading ->
            if (isDataLoading) {
                binding.eventPb.visibility = View.VISIBLE
            } else {
                binding.eventPb.visibility = View.INVISIBLE
            }
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)

        activity?.toolbar?.menu?.findItem(R.id.item_share)?.isVisible = true
        editItem?.isVisible = false
        deleteItem?.isVisible = false

    }

    private fun configureRecyclerView() {
        adapter = EventsAdapter(getFirestoreRecyclerOptions(), eventViewModel)
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewEvents.adapter = adapter
        adapter.startListening()

    }

    private fun getFirestoreRecyclerOptions(): FirestoreRecyclerOptions<SchoolEvent> {
        val query = eventViewModel.query.value
        return FirestoreRecyclerOptions.Builder<SchoolEvent>()
            .setQuery(query!!, SchoolEvent::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    /*private fun setupListenerOnFub() {
            binding.fubNotice.setOnClickListener {
                showANoticeDetail()
            }
    }*//*

    private fun showANoticeDetail() {
        findNavController().navigate(R.id.action_noticeFragment_to_noticeDetailFragment)
        adapter.stopListening()
    }
*/
    private fun hideProgress() {
        pb_notice.visibility = View.INVISIBLE
    }



}

