package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.bogiruapps.rdshapp.R

class NoticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val safeArgs = NoticeFragmentArgs.fromBundle(arguments)

        if (!safeArgs.hasSchool) {
            findNavController().navigate(R.id.choseSchoolFragment)
        }


        return inflater.inflate(R.layout.fragment_notice, container, false)
    }


}
