package com.bogiruapps.rdshapp.splash


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.bogiruapps.rdshapp.R


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        viewModel.isChangeSchool.observe(this, Observer {
            if (it) {
                val action = LoadFragmentDirections.action_loadFragment_to_noticeFragment()
                action.setHasSchool(viewModel.hasSchool.value!!)
                findNavController().navigate(action)
            }
        })

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


}
