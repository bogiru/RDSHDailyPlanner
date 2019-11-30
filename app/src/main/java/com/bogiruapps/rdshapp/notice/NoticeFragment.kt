package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.fragment_notice_empty.view.*

class NoticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View
        val args = NoticeFragmentArgs.fromBundle(arguments!!)

        if (args.hasSchool) {

            val list = listOf<String>("tom", "red", "blue", "dsfdsfdsf", "dfdfggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggd", "rerytryt", "4543543534", "fgtruyiuyjh", "fghtfhgf", "dfdsfdsfsd")

            view = inflater.inflate(R.layout.fragment_notice, container, false)
            val recyclerView = view.recycler_view_notices.apply {
                layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = NoticeAdapter(list)
            }

        }else {
            view = inflater.inflate(R.layout.fragment_notice_empty, container, false)
            view.btn_apply_school.setOnClickListener {
                findNavController().navigate(R.id.choseSchoolFragment)
            }
        }

        return view
    }


}
