package com.bogiruapps.rdshapp.events


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventsBinding
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
        configureFirebase()
        configureToolbar()
        configureRecyclerView()
       // setupListenerOnFub()

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
    }

    /*@SuppressLint("ResourceType")
    private fun showAllertDialogDelete(){
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle("Удалить объявление")
        alertBuilder.setMessage("Вы уверены, что хотите удалить объявление?")
        alertBuilder.setIconAttribute(R.drawable.rdsh_image)
        alertBuilder.setCancelable(true)
        alertBuilder.setPositiveButton(
            "Да"
        ) { _: DialogInterface, _: Int ->
            eventViewModel.deleteNotice()
        }
        alertBuilder.setNegativeButton(
            "Нет"
        ) { _: DialogInterface, _: Int ->
        }
        alertBuilder.show()
    }
*/



    private fun configureFirebase() {
        val auth = FirebaseAuth.getInstance()
        //.checkUserSchool()
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)

        activity?.toolbar?.menu?.findItem(R.id.item_share)?.isVisible = true
        editItem?.isVisible = false
        deleteItem?.isVisible = false

    }

    private fun configureRecyclerView() {
        adapter = EventsAdapter(eventViewModel.fetchFirestoreRecyclerOptions(), eventViewModel)
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewEvents.adapter = adapter
        adapter.startListening()

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

