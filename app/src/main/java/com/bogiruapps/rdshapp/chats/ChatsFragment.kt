package com.bogiruapps.rdshapp.chats


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
import com.bogiruapps.rdshapp.databinding.FragmentChatsBinding
import com.bogiruapps.rdshapp.utils.hideBottomNavigationView
import com.bogiruapps.rdshapp.utils.hideKeyboard
import com.bogiruapps.rdshapp.utils.showSnackbar
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ChatsFragment : Fragment() {

    private val chatsViewModel: ChatsViewModel by viewModel()

    private lateinit var adapter: ChatsAdapter
    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        chatsViewModel.fetchFirestoreRecyclerQuery()
        setupObserverViewModel()
        configureToolbar()
        hideBottomNavigationView(activity!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.hideKeyboard()
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false)
        binding.viewModel = chatsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {

        chatsViewModel.queryChats.observe(viewLifecycleOwner, Observer { queryChats ->
            configureRecyclerView(queryChats)
        })

        chatsViewModel.openChatRoomEventFragment.observe(viewLifecycleOwner, EventObserver {
            openChatRoomEventFragment()
        })

        chatsViewModel.showSnackbar.observe(viewLifecycleOwner, Observer { message ->
            showSnackbar(view!!, message)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureRecyclerView(queryChats: Query) {
        val options = getFirestoreRecyclerOptions(queryChats)
        adapter = ChatsAdapter(options, chatsViewModel)
        binding.recyclerViewChats.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewChats.adapter = adapter
    }

    private fun getFirestoreRecyclerOptions(queryChats: Query): FirestoreRecyclerOptions<Chat> {
        return FirestoreRecyclerOptions.Builder<Chat>()
            .setQuery(queryChats, Chat::class.java)
            .setLifecycleOwner(this)
            .build()
    }

    private fun openChatRoomEventFragment() {
        findNavController().navigate(R.id.action_chatsFragment_to_eventChatRoomFragment)
    }

}
