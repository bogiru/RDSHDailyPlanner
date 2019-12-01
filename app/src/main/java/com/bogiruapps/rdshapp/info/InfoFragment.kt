package com.bogiruapps.rdshapp.info


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.bogiruapps.rdshapp.R
import com.bogiruapps.rdshapp.databinding.FragmentInfoBinding
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
        return binding.root
    }


}
