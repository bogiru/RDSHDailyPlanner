package com.bogiruapps.rdshapp.info


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentInfoBinding
import kotlinx.android.synthetic.main.activity_main.*


class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureBinding(inflater, container)
        configureToolbar()
        hideBottomNavigation()
        configureDotsIndicator()

        return binding.root
    }

    private fun configureBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun configureToolbar() {
        val editItem = activity?.main_toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.main_toolbar?.menu?.findItem(R.id.item_delete)
        editItem?.isVisible = false
        deleteItem?.isVisible = false

        activity?.main_toolbar?.title = " "
    }

    private fun hideBottomNavigation() {
        activity!!.bottomNavigationView.visibility = View.GONE
    }

    private fun configureDotsIndicator() {
        val wormDotsIndicator = binding.infoSpringDotsIndicator
        val viewPager = binding.infoViewPager
        val adapter = InfoViewPagerAdapter()
        viewPager.adapter = adapter
        wormDotsIndicator.setViewPager2(viewPager)
    }
}
