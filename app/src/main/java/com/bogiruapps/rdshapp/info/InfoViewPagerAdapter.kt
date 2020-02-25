package com.bogiruapps.rdshapp.info

import android.app.Application
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bogiruapps.rdshapp.R
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.android.synthetic.main.item_page_info.view.*

class InfoViewPagerAdapter(val application: Application) : RecyclerView.Adapter<InfoViewPagerAdapter.InfoHolder>() {

    val pages = listOf<Page>(
        Page(R.string.info_text1, R.drawable.rdsh_image6),
        Page(R.string.info_text2, R.drawable.info_image21),
        Page(R.string.info_text3, R.drawable.info_image3),
        Page(R.string.info_text4, R.drawable.info_image4),
        Page(R.string.info_text5, R.drawable.info_image5)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page_info, parent, false)
        return InfoHolder(view)
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.itemView.info_text_view.text = application.resources.getString(pages[position].indexText)
        holder.itemView.info_image.setImageDrawable(application.getDrawable(pages[position].image_src))
    }

    class InfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    data class Page(
        val indexText: Int,
        val image_src: Int
    )

}