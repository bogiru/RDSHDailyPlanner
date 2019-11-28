package com.bogiruapps.rdshapp.splash


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.bogiruapps.rdshapp.R


class SplashFragment : Fragment() {

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashViewModel.checkHasSchool()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("QWE", "SplashFragment")
        super.onViewCreated(view, savedInstanceState)
        splashViewModel.isChangeSchool.observe(this, Observer {
            if (it) {
                splashViewModel.isChangeSchool.value = false
                findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToNoticeFragment(
                        splashViewModel.hasSchool.value!!
                    )
                )


                /*val action = SplashFragmentDirections.actionSplashFragmentToNoticeFragment()
                action.hasSchool = splashViewModel.hasSchool.value!!
                findNavController().navigate(action)*/
            }
        })
    }


}
