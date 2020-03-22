package com.bogiruapps.rdshapp.info

    import android.app.Application
    import android.os.Build
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.annotation.RequiresApi
    import androidx.fragment.app.FragmentManager
    import androidx.recyclerview.widget.RecyclerView
    import com.bogiruapps.rdshapp.R
    import com.bogiruapps.rdshapp.databinding.EventsItemBinding
    import com.bogiruapps.rdshapp.databinding.FragmentInfoBinding
    import com.bogiruapps.rdshapp.databinding.ItemPageInfoBinding
    import com.bogiruapps.rdshapp.events.EventsAdapter
    import com.bogiruapps.rdshapp.events.EventsViewModel
    import com.bogiruapps.rdshapp.events.SchoolEvent
    import kotlinx.android.synthetic.main.item_page_info.view.*

class InfoViewPagerAdapter () : RecyclerView.Adapter<InfoViewPagerAdapter.InfoHolder>() {

    private val pages = listOf(
        InfoPage(R.string.info_text1, "Российское движение школьников"),
        InfoPage(R.string.info_text2, "Личностное развитие"),
        InfoPage(R.string.info_text3, "Гражданская активность"),
        InfoPage(R.string.info_text4, "Медийное направление"),
        InfoPage(R.string.info_text5, "Военно-патриотическое направление")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        return InfoHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.bind(pages[position])
    }

    class InfoHolder(private val binding: ItemPageInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(page: InfoPage) {
            binding.page = page
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): InfoHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPageInfoBinding.inflate(layoutInflater, parent, false)
                return InfoViewPagerAdapter.InfoHolder(binding)
            }
        }
    }

}