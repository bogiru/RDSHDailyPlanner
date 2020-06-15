package com.bogiruapps.rdshapp.schoolEvents.tasksEvent


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
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class SchoolTasksEventFragment : Fragment() {

    private val schoolTaskEventViewModel: SchoolTaskEventViewModel by viewModel()

    private lateinit var adapterSchool: SchoolTaskEventAdapter
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

    private fun setupObserverViewModel() {
        schoolTaskEventViewModel.openTaskEventEdit.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_tasksEventFragment_to_taskEventEditFragment)
        })

        schoolTaskEventViewModel.query.observe(viewLifecycleOwner, Observer { query ->
            configureRecyclerView(query)
        })

        schoolTaskEventViewModel.openSchoolTaskEventDeleteFragmentEvent.observe(viewLifecycleOwner, EventObserver {
            showAllertDialogDelete(it)
        })

        schoolTaskEventViewModel.showSnackbar.observe(viewLifecycleOwner, EventObserver { message ->
            showSnackbar(view!!, message)
        })

    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks_event, container, false)
        binding.viewModel = schoolTaskEventViewModel

        if (!schoolTaskEventViewModel.checkUserIsAuthorEvent()) {
            binding.taskEventFub.visibility = View.INVISIBLE
        }

        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun configureRecyclerView(query: Query) {
        adapterSchool = SchoolTaskEventAdapter(getFirestoreRecyclerOptions(query), schoolTaskEventViewModel)
        binding.taskEventRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.taskEventRecyclerView.adapter = adapterSchool

        val callback = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (schoolTaskEventViewModel.checkUserIsAuthorEvent()) {
                    adapterSchool.deleteItem(viewHolder.adapterPosition)
                } else {
                    adapterSchool.notifyItemChanged(viewHolder.adapterPosition)
                    showSnackbar(view!!, "Право удаления предоставлено только автору мероприятия")
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.taskEventRecyclerView)
    }

    private fun getFirestoreRecyclerOptions(query: Query): FirestoreRecyclerOptions<SchoolTaskEvent> {
        return FirestoreRecyclerOptions.Builder<SchoolTaskEvent>()
            .setQuery(query!!, SchoolTaskEvent::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.main_toolbar?.title = "Задачи"
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    @SuppressLint("ResourceType")
    private fun showAllertDialogDelete(schoolTaskEvent: SchoolTaskEvent){
        val alertBuilder = AlertDialog.Builder(activity, R.style.AlertDialogTheme)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            schoolTaskEventViewModel.deleteTaskEvent(schoolTaskEvent)
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }

}
