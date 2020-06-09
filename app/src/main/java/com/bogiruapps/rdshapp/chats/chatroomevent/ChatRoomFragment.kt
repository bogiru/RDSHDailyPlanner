package com.bogiruapps.rdshapp.chats.chatroomevent


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentEventChatRoomBinding
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class ChatRoomFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by viewModel()

    private lateinit var adapter: ChatRoomAdapter
    private lateinit var binding: FragmentEventChatRoomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        chatRoomViewModel.fetchFirestoreRecyclerQuery()
        configureToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }


    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_chat_room, container, false)
        binding.viewModel = chatRoomViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }


    private fun setupObserverViewModel() {
        chatRoomViewModel.showEventChatRoomContent.observe(viewLifecycleOwner, EventObserver {
            configureRecyclerView()
        })

        chatRoomViewModel.updateEventChatRoomRecyclerView.observe(viewLifecycleOwner, EventObserver {
            binding.eventChatRoomRecyclerView.smoothScrollToPosition(0)
        })

        chatRoomViewModel.clearEventChatRoomEdtText.observe(viewLifecycleOwner, EventObserver {
            binding.eventChatRoomEdtText.text.clear()
            this.hideKeyboard()
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)

        activity?.main_toolbar?.title = "Чат"
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true

        adapter = ChatRoomAdapter(getFirestoreRecyclerOptions(), chatRoomViewModel)
        binding.eventChatRoomRecyclerView.layoutManager = layoutManager
        binding.eventChatRoomRecyclerView.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(): FirestoreRecyclerOptions<Message> {
        val query = chatRoomViewModel.query.value
        return FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query!!, Message::class.java)
            .setLifecycleOwner(this)
            .build()
    }

}

