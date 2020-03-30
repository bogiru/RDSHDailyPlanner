package com.bogiruapps.rdshapp.info

    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import com.bogiruapps.rdshapp.R
    import com.bogiruapps.rdshapp.databinding.ItemPageInfoBinding

class InfoViewPagerAdapter () : RecyclerView.Adapter<InfoViewPagerAdapter.InfoHolder>() {

    private val pages = listOf(
        InfoPage(R.string.info_text1, R.drawable.info_image_rdsh),
        InfoPage(R.string.info_text2, R.drawable.info_image_personal_development),
        InfoPage(R.string.info_text3, R.drawable.info_image_civic_engagement),
        InfoPage(R.string.info_text4, R.drawable.info_image_media),
        InfoPage(R.string.info_text5, R.drawable.info_image_military_patriotic)
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