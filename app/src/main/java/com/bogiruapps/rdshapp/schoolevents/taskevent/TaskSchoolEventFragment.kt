package com.bogiruapps.rdshapp.schoolevents.taskevent


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentTasksEventBinding
import com.bogiruapps.rdshapp.utils.SwipeToDeleteCallback
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class TaskSchoolEventFragment : Fragment() {

    private val taskSchoolEventViewModel: TaskSchoolEventViewModel by viewModel()

    private lateinit var adapter: TaskSchoolEventAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater
            .from(context)
            .inflateTransition(android.R.transition.move)
    }   

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_event, container, false)
        binding.viewModel = taskSchoolEventViewModel

        if (!taskSchoolEventViewModel.checkUserIsAuthorSchoolEvent()) {
            binding.taskEventFub.visibility = View.INVISIBLE
        }

        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)

        editItem?.isVisible = false
        deleteItem?.isVisible = false
        
        activity?.main_toolbar?.navigationIcon = null
    }

    private fun setupObserverViewModel() {
        taskSchoolEventViewModel.openTaskSchoolEventEdit.observe(viewLifecycleOwner, EventObserver {
            openTaskSchoolEventEditFragment()
        })

        taskSchoolEventViewModel.queryTasksSchoolEvent.observe(viewLifecycleOwner, Observer { query ->
            configureRecyclerView(query)
        })

        taskSchoolEventViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureRecyclerView(queryTasksSchoolEvent: Query) {
        adapter = TaskSchoolEventAdapter(
            getFirestoreRecyclerOptions(queryTasksSchoolEvent),
            taskSchoolEventViewModel
        )
        binding.taskEventRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.taskEventRecyclerView.adapter = adapter

        val callback = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (taskSchoolEventViewModel.checkUserIsAuthorSchoolEvent()) {
                    adapter.deleteItem(viewHolder.adapterPosition)
                } else {
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                    showSnackbar(view!!, activity!!.application.resources
                        .getString(R.string.error_not_enough_rights_to_delete))
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.taskEventRecyclerView)
    }

    private fun getFirestoreRecyclerOptions(queryTasksSchoolEvent: Query)
            : FirestoreRecyclerOptions<TaskSchoolEvent> =
        FirestoreRecyclerOptions
            .Builder<TaskSchoolEvent>()
            .setQuery(queryTasksSchoolEvent, TaskSchoolEvent::class.java)
            .setLifecycleOwner(this)
            .build()

    private fun openTaskSchoolEventEditFragment() {
        findNavController().navigate(R.id.action_taskSchoolEventFragment_to_taskSchoolEventEditFragment)
    }

    private fun openSchoolEventDetailFragment() {
        findNavController().navigate(R.id.action_tasksSchoolEventFragment_to_schoolEventDetailFragment)
    }
}
