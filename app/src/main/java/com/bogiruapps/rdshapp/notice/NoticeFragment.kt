package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController

import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_notice_empty.*
import kotlinx.android.synthetic.main.fragment_notice_empty.view.*

class NoticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View
        val args = NoticeFragmentArgs.fromBundle(arguments!!)

        if (args.hasSchool) {
            view = inflater.inflate(R.layout.fragment_notice, container, false)

        }else {
            view = inflater.inflate(R.layout.fragment_notice_empty, container, false)
            view.btn_apply_school.setOnClickListener {
                findNavController().navigate(R.id.choseSchoolFragment)
            }
        }

        return view
    }


}
