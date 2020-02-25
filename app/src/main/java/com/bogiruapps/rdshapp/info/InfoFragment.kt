package com.bogiruapps.rdshapp.info


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentInfoBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_info.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentInfoBinding>(inflater, R.layout.fragment_info, container, false)
         binding.viewPager.adapter = activity?.application?.let { InfoViewPagerAdapter(it) }
        configureToolbar()
        return binding.root
    }

    private fun configureToolbar() {
        val editItem = activity?.toolbar?.menu?.findItem(R.id.item_edit)
        val deleteItem = activity?.toolbar?.menu?.findItem(R.id.item_delete)
        val image = activity!!.headerImage

        activity?.window?.decorView?.systemUiVisibility = View.VISIBLE
        activity?.collapseToolbar?.title = "Информация"
        activity?.appBar?.setExpanded(false)
        editItem?.isVisible = false
        deleteItem?.isVisible = false
    }

}
