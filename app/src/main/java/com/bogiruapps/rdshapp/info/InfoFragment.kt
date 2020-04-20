package com.bogiruapps.rdshapp.info


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentInfoBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel


class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        configureToolbar()
        configureDotsIndicator()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

    private fun configureDotsIndicator() {
        val wormDotsIndicator = binding.springDotsIndicator
        val viewPager = binding.viewPager
        val adapter = InfoViewPagerAdapter()
        viewPager.adapter = adapter
        wormDotsIndicator.setViewPager2(viewPager)
    }
}
