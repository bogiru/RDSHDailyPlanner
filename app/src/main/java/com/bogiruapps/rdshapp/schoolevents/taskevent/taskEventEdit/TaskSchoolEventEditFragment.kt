package com.bogiruapps.rdshapp.schoolevents.taskevent.taskEventEdit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.databinding.FragmentTaskEventEditBinding
import com.bogiruapps.rdshapp.utils.showSnackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class TaskSchoolEventEditFragment : Fragment() {

    private val taskEventViewModel: TaskSchoolEventEditViewModel by viewModel()

    private lateinit var binding: FragmentTaskEventEditBinding

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_task_event_edit,
            container,
            false)
        binding.viewModel = taskEventViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        taskEventViewModel.openTaskSchoolEventFragment.observe(viewLifecycleOwner, EventObserver {
            openTaskSchoolEventFragment()
        })

        taskEventViewModel.users.observe(viewLifecycleOwner, Observer {
            setupSpinner(it)
        })

        taskEventViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun setupSpinner(users: List<User?>) {
        val spinnerAdapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            users
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.taskEventEditSpinner.adapter = spinnerAdapter

        binding.taskEventEditSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskEventViewModel.taskSchoolEvent.user = users[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun openTaskSchoolEventFragment() {
        findNavController().navigate(R.id.action_taskSchoolEventEditFragment_to_taskSchoolEventFragment)
    }

}
