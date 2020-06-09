package com.bogiruapps.rdshapp.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.RatingFragmentBinding
import com.bogiruapps.rdshapp.user.User
import com.bogiruapps.rdshapp.utils.hideBottomNavigationView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class RatingFragment : Fragment() {

    private val ratingViewModel: RatingViewModel by viewModel()

    private lateinit var adapter: RatingAdapter
    private lateinit var binding: RatingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        configureBinding(inflater, container)
        setupObserverViewModel()
        configureToolbar()
        hideBottomNavigationView(activity!!)
        ratingViewModel.fetchFirestoreRecyclerQuery()
        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.rating_fragment, container, false)
        binding.viewModel = ratingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupObserverViewModel() {
        ratingViewModel.queryUsers.observe(viewLifecycleOwner, Observer {
            configureRecyclerView(it)
        })
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureRecyclerView(query: Query) {
        val options = getFirestoreRecyclerOptions(query)
        adapter = RatingAdapter(options, ratingViewModel)
        binding.ratingRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.ratingRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun getFirestoreRecyclerOptions(query: Query): FirestoreRecyclerOptions<User> {
        return FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .setLifecycleOwner(this)
            .build()
    }

}
