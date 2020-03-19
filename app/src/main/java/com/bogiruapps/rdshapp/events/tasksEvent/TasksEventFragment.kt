package com.bogiruapps.rdshapp.events.tasksEvent


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupObserverViewModel() {
        taskEventViewModel.openTaskEventEdit.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_tasksEventFragment_to_taskEventEditFragment)
        })

        taskEventViewModel.query.observe(viewLifecycleOwner, Observer {query ->
            configureRecyclerView(query)
        })

        taskEventViewModel.dataLoading.observe(viewLifecycleOwner, Observer { isDataLoading ->
            if (isDataLoading) {
                binding.taskEventPb.visibility = View.VISIBLE
            } else {
                binding.taskEventPb.visibility = View.INVISIBLE
            }
        })

        taskEventViewModel.openTaskEventDeleteFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showAllertDialogDelete(it)
        })

        taskEventViewModel.showSnackbar.observe(viewLifecycleOwner, EventObserver {
            showSnackbar(it)
        })

    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_event, container, false)
        binding.viewModel = taskEventViewModel

        if (!taskEventViewModel.checkUserIsAuthorEvent()) {
            binding.fubTaskEvent.visibility = View.INVISIBLE
        }

        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun configureRecyclerView(query: Query) {
        adapter = TaskEventAdapter(getFirestoreRecyclerOptions(query), taskEventViewModel)
        binding.recyclerViewTasksEvent.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewTasksEvent.adapter = adapter

        val callback = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (taskEventViewModel.checkUserIsAuthorEvent()) {
                    adapter.deleteItem(viewHolder.adapterPosition)
                } else {
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                    taskEventViewModel.showSnackbar("Право удаления предоставлено только автору мероприятия")
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTasksEvent)
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

    @SuppressLint("ResourceType")
    private fun showAllertDialogDelete(taskEvent: TaskEvent){
        val alertBuilder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            taskEventViewModel.deleteTaskEvent(taskEvent)
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }

}
