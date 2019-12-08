package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogiruapps.rdshapp.FirestoreRepository

import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.fragment_choose_school.view.*
import kotlinx.android.synthetic.main.fragment_notice.view.*
import kotlinx.android.synthetic.main.notice_empty.view.*
import kotlinx.android.synthetic.main.notice_full.view.*

class NoticeFragment : Fragment() {

    private lateinit var noticeViewModel: NoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repository = FirestoreRepository.getInstance(activity!!.application)
        val factory = NoticeViewModelFactory(repository)
        noticeViewModel = ViewModelProviders.of(this, factory).get(NoticeViewModel::class.java)
        noticeViewModel.checkHasSchool()

        return inflater.inflate(R.layout.fragment_notice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noticeViewModel.isCheckSchool.observe(viewLifecycleOwner, Observer { check ->
            if (check) {
                view.pb_notice.visibility = View.INVISIBLE
                if (noticeViewModel.hasSchool.value!!) {
                    view.include_notice_full.visibility = View.VISIBLE
                    initRecycleView(view)

                } else {
                    view.include_notice_empty.visibility = View.VISIBLE
                    view.include_notice_empty.btn_apply_school.setOnClickListener {
                        findNavController().navigate(R.id.choseSchoolFragment)
                    }

                }
            }
        })
    }

    private fun initRecycleView(view: View) {
        noticeViewModel.getNotices()

        noticeViewModel.isLoadSchools.observe(viewLifecycleOwner, Observer {isLoad ->
            if (isLoad) {
                val notices = noticeViewModel.notices
                view.include_notice_full.recycler_view_notice.apply {
                    layoutManager =
                        LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = NoticeAdapter(notices)
                }
            }
        })
    }

}
