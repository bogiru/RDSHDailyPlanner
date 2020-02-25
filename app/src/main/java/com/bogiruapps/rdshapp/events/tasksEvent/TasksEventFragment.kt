package com.bogiruapps.rdshapp.events.tasksEvent


import android.os.Bundle
import android.transition.TransitionInflater
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
import com.bogiruapps.rdshapp.databinding.FragmentTasksEventBinding
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
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
        configureToolbar()
        setupObserverViewModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }


    private fun setupObserverViewModel() {
        taskEventViewModel.openTaskEventEdit.observe(this, EventObserver {
            findNavController().navigate(R.id.action_tasksEventFragment_to_taskEventEditFragment)
        })

        taskEventViewModel.query.observe(this, Observer {query ->
            configureRecyclerView(query)
        })

        taskEventViewModel.dataLoading.observe(this, Observer { isDataLoading ->
            if (isDataLoading) {
                binding.taskEventPb.visibility = View.VISIBLE
            } else {
                binding.taskEventPb.visibility = View.INVISIBLE
            }
        })

    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_event, container, false)
        binding.viewModel = taskEventViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner


    }

    private fun configureRecyclerView(query: Query) {
        adapter = TaskEventAdapter(getFirestoreRecyclerOptions(query), taskEventViewModel)
        binding.recyclerViewTasksEvent.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewTasksEvent.adapter = adapter
        adapter.startListening()

    }

    private fun getFirestoreRecyclerOptions(query: Query): FirestoreRecyclerOptions<TaskEvent> {
        return FirestoreRecyclerOptions.Builder<TaskEvent>()
            .setQuery(query!!, TaskEvent::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.collapseToolbar?.title = "Задачи"
        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }
}
