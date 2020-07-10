package com.bogiruapps.rdshapp.chats.chatroomevent


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.EventObserver

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentMessageBinding
import com.bogiruapps.rdshapp.databinding.MessageItemBinding
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChatRoomFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by viewModel()

    private lateinit var adapter: ChatRoomAdapter
    private lateinit var binding: FragmentMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        setupObserverViewModel()
        chatRoomViewModel.fetchFirestoreRecyclerQueryMessages()
        configureToolbar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_message,
            container,
            false)
        binding.viewModel = chatRoomViewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
    }


    private fun setupObserverViewModel() {
        chatRoomViewModel.queryMessages.observe(viewLifecycleOwner, Observer { queryMessages ->
            configureRecyclerView(queryMessages)
        })

        chatRoomViewModel.updateChatRoomRecyclerView.observe(viewLifecycleOwner, EventObserver {
            binding.messageRecyclerView.smoothScrollToPosition(0)
        })

        chatRoomViewModel.clearChatRoomEdtText.observe(viewLifecycleOwner, EventObserver {
            binding.messageEdtText.text.clear()
            this.hideKeyboard()
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false

        activity?.main_toolbar?.title = chatRoomViewModel.chat.title

        if (activity?.bottomNavigationView?.visibility == View.VISIBLE) {
            activity?.main_toolbar?.navigationIcon = null
        }

    }

    private fun configureRecyclerView(queryMessages: Query) {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true

        adapter = ChatRoomAdapter(getFirestoreRecyclerOptions(queryMessages), chatRoomViewModel)
        binding.messageRecyclerView.layoutManager = layoutManager
        binding.messageRecyclerView.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(queryMessages: Query): FirestoreRecyclerOptions<Message> {
        return FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(queryMessages, Message::class.java)
            .setLifecycleOwner(this)
            .build()
    }

}

