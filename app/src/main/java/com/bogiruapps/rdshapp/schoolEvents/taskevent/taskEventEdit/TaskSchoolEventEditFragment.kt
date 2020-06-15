package com.bogiruapps.rdshapp.schoolEvents.taskevent.taskEventEdit

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

    private val taskEventviewModel: TaskSchoolEventEditViewModel by viewModel()

    private lateinit var binding: FragmentTaskEventEditBinding
    private lateinit var chosenUser: User

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_event_edit, container, false)
        binding.viewModel = taskEventviewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        taskEventviewModel.openTaskEventFragment.observe(this, EventObserver {
            findNavController().navigate(R.id.action_taskEventEditFragment_to_tasksEventFragment)
        })

        taskEventviewModel.users.observe(this, Observer {
            setupSpinner(it)
        })

        taskEventviewModel.showSnackbar.observe(this, Observer {message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.main_toolbar?.title = "Редактирование"
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun setupSpinner(items: List<User?>) {
        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(context!!, android.R.layout.simple_spinner_item, getUserNames(items))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.taskEventEditSpinner.adapter = spinnerAdapter

        binding.taskEventEditSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskEventviewModel.taskSchoolEvent.user = items[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun getUserNames(users: List<User?>): List<String> {
        val userNames = mutableListOf<String>()
        for (user in users) userNames.add(user!!.name!!)
        return userNames
    }

}
