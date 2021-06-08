package com.bangkit.elevate.ui.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.databinding.ItemListIdeaBinding
import com.bangkit.elevate.utils.loadImage
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(private val homeClickCallback: HomeClickCallback) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var listIdeas = ArrayList<IdeaEntity>()

    fun setListIdeas(ideas: List<IdeaEntity>?) {
        if (ideas == null) return
        this.listIdeas.clear()
        this.listIdeas.addAll(ideas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.HomeViewHolder {
        val itemListIdeasBinding =
            ItemListIdeaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemListIdeasBinding)
    }

    override fun onBindViewHolder(holder: HomeAdapter.HomeViewHolder, position: Int) {
        val ideas = listIdeas[position]
        holder.bind(ideas)
    }

    override fun getItemCount(): Int = listIdeas.size

    inner class HomeViewHolder(private val binding: ItemListIdeaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ideas: IdeaEntity) {
            with(binding) {

                itemView.setOnClickListener {
                    homeClickCallback.onItemClicked(ideas)
                }

                val localeId = Locale("in", "ID")
                val priceFormat = NumberFormat.getCurrencyInstance(localeId)
                cardBrandName.text = ideas.brandName
                cardBrandType.text = ideas.businessIdea
                cardLocation.text = ideas.location
                cardIdeaStatus.text = ideas.status
                cardTotalFund.text = priceFormat.format(ideas.requiredCost)
                cardProgressBar.progress = 0
                imgBrand.loadImage(ideas.logoFile)

            }
        }
    }
}