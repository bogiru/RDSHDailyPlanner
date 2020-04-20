package com.bogiruapps.rdshapp.events.chat_room_event


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
import com.bogiruapps.rdshapp.databinding.FragmentEventChatRoomBinding
import com.bogiruapps.rdshapp.events.EventsAdapter
import com.bogiruapps.rdshapp.events.EventsViewModel
import com.bogiruapps.rdshapp.events.SchoolEvent
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notice.*
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class EventChatRoomFragment : Fragment() {

    private val eventChatRoomViewModel: EventChatRoomViewModel by viewModel()

    private lateinit var adapter: EventChatRoomAdapter
    private lateinit var binding: FragmentEventChatRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        eventChatRoomViewModel.fetchFirestoreRecyclerQuery()
        configureToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }


    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_chat_room, container, false)
        binding.viewModel = eventChatRoomViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }


    private fun setupObserverViewModel() {
        eventChatRoomViewModel.showEventChatRoomContent.observe(viewLifecycleOwner, EventObserver {
            configureRecyclerView()
        })

        eventChatRoomViewModel.updateEventChatRoomRecyclerView.observe(viewLifecycleOwner, EventObserver {
            binding.eventChatRoomRecyclerView.smoothScrollToPosition(0)
        })

        eventChatRoomViewModel.clearEventChatRoomEdtText.observe(viewLifecycleOwner, EventObserver {
            binding.eventChatRoomEdtText.text.clear()
            this.hideKeyboard()
        })

        eventChatRoomViewModel.dataLoading.observe(viewLifecycleOwner, Observer { isDataLoading ->
            if (isDataLoading) {
                binding.eventChatRoomPb.visibility = View.VISIBLE
            } else {
                binding.eventChatRoomPb.visibility = View.INVISIBLE
            }
        })
    }
    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.toolbar?.title = "Чат"
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true

        adapter = EventChatRoomAdapter(getFirestoreRecyclerOptions(), eventChatRoomViewModel)
        binding.eventChatRoomRecyclerView.layoutManager = layoutManager
        binding.eventChatRoomRecyclerView.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Message> {
        val query = eventChatRoomViewModel.query.value
        return FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query!!, Message::class.java)
            .setLifecycleOwner(this)
            .build()
    }

}

