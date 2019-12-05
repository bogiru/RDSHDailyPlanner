package com.bogiruapps.rdshapp.notice


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bogiruapps.rdshapp.FirestoreRepository

import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.fragment_notice.view.*

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

                } else {
                    view.include_notice_empty.visibility = View.VISIBLE

                }
            }
        })

    }

}
