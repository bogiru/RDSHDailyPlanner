package com.bogiruapps.rdshapp.events.tasksEvent


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventsBinding
import com.bogiruapps.rdshapp.databinding.FragmentTasksEventBinding
import com.bogiruapps.rdshapp.events.EventViewModel
import com.bogiruapps.rdshapp.events.EventsAdapter
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class TasksEventFragment : Fragment() {

    private val taskEventViewModel: TaskEventViewModel by viewModel()

    private lateinit var adapter: TaskEventAdapter
    private lateinit var binding: FragmentTasksEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        configureRecyclerView()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_event, container, false)
        binding.viewModel = taskEventViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun configureRecyclerView() {
        adapter = TaskEventAdapter(taskEventViewModel.fetchFirestoreRecyclerOptions(), taskEventViewModel)
        binding.recyclerViewTasksEvent.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewTasksEvent.adapter = adapter
        adapter.startListening()

    }


}
