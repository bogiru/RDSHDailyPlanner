package com.bogiruapps.rdshapp.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.notice_item.view.*

class NoticeAdapter(private val notices: List<Notice>) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notice_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = notices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.notice_text_view.text = notices[position].text
    }


class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}