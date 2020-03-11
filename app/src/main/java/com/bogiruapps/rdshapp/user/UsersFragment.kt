package com.bogiruapps.rdshapp.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.UsersFragmentBinding
import com.bogiruapps.rdshapp.utils.GRID_SPAN_COUNT
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class UsersFragment : Fragment() {

    private val usersViewModel: UsersViewModel by viewModel()

    private lateinit var adapter: UsersAdapter
    private lateinit var binding: UsersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        configureBinding(inflater, container)
        setupObserverViewModel()
        configureToolbar()
        usersViewModel.fetchFirestoreRecyclerQuery()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.users_fragment, container, false)
        binding.viewModel = usersViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {

        usersViewModel.query.observe(this, Observer {
            configureRecyclerView(it)
        })

        usersViewModel.dataLoading.observe(this, Observer{ isShowProgress ->
            if (isShowProgress) {
                binding.usersPb.visibility = View.VISIBLE
            } else {
                binding.usersPb.visibility = View.INVISIBLE
            }
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.collapseToolbar?.title = "Рейтинг"
        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureRecyclerView(query: Query) {
       // val layoutManager = GridLayoutManager(activity, GRID_SPAN_COUNT, GridLayoutManager.HORIZONTAL, false)
        val options = getFirestoreRecyclerOptions(query)
        adapter = UsersAdapter(options, usersViewModel)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewUsers.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getFirestoreRecyclerOptions(query: Query): FirestoreRecyclerOptions<User> {
        return FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query!!, User::class.java)
            .setLifecycleOwner(this)
            .build()
    }

}
