package com.bangkit.elevate.ui.dashboard.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.elevate.data.IdeaEntity
import com.bangkit.elevate.databinding.ItemListIdeaBinding
import com.bangkit.elevate.utils.loadImage

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
//            val currentFunding = ideas.currentFund.toDouble()
//            val requiredCost = ideas.requiredCost.toDouble()
            with(binding) {

                itemView.setOnClickListener {
                    homeClickCallback.onItemClicked(ideas)
                }
                cardBrandName.text = ideas.brandName
                cardBrandType.text = ideas.businessIdea
                cardLocation.text = ideas.location
                cardIdeaStatus.text = ideas.status
                cardTotalFund.text = ideas.requiredCost.toString()
                cardProgressBar.progress = 0
                cardImage.loadImage(ideas.logoFile)

            }
            Log.d("DataEachIdea", ideas.toString())
        }
    }
}