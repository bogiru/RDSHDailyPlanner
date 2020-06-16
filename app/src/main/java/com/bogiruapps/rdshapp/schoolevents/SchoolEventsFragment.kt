package com.bogiruapps.rdshapp.schoolevents


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
import com.bogiruapps.rdshapp.databinding.FragmentSchoolEventsBinding
import com.bogiruapps.rdshapp.utils.hideBottomNavigationView
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class SchoolEventsFragment : Fragment() {

    private val schoolEventsViewModel: SchoolEventsViewModel by viewModel()

    private lateinit var adapter: SchoolEventsAdapter
    private lateinit var binding: FragmentSchoolEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        schoolEventsViewModel.fetchFirestoreRecyclerQuerySchoolEvents()
        configureToolbar()
        hideBottomNavigationView(activity!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_school_events, container, false)
        binding.viewModel = schoolEventsViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        schoolEventsViewModel.openSchoolEventDetailFragment.observe(viewLifecycleOwner, EventObserver {
            openSchoolEventDetailFragment()
        })

        schoolEventsViewModel.openSchoolEventEditFragment.observe(viewLifecycleOwner, EventObserver {
            openSchoolEventEditFragment()
        })

        schoolEventsViewModel.showSchoolEventContent.observe(viewLifecycleOwner, EventObserver {
            configureRecyclerView()
        })

        schoolEventsViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureRecyclerView() {
        adapter = SchoolEventsAdapter(getFirestoreRecyclerOptions(), schoolEventsViewModel)
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.eventsRecyclerView.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(): FirestoreRecyclerOptions<SchoolEvent> {
        val queryEvents = schoolEventsViewModel.querySchoolEvents.value
        return FirestoreRecyclerOptions.Builder<SchoolEvent>()
            .setQuery(queryEvents!!, SchoolEvent::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun openSchoolEventDetailFragment() {
        findNavController().navigate(R.id.action_schoolEventsFragment_to_schoolEventDetailFragment)
    }

    private fun openSchoolEventEditFragment() {
        findNavController().navigate(R.id.action_schoolEventsFragment_to_schoolEventEditFragment)
    }
}

