package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsIcebreakerQuestionBinding
import com.app.shotclock.models.IceBreakerQuestionResponse

class IcebreakerAdapter(private var list: ArrayList<IceBreakerQuestionResponse.IceBreakerBody>) : RecyclerView.Adapter<IcebreakerAdapter.BreakerHolder>() {

    class BreakerHolder(itemsView : ItemsIcebreakerQuestionBinding) : RecyclerView.ViewHolder(itemsView.root){
        val itemBinding : ItemsIcebreakerQuestionBinding = itemsView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsIcebreakerQuestionBinding.inflate(inflater,parent,false)
        return BreakerHolder(binding)
    }

    override fun onBindViewHolder(holder: BreakerHolder, position: Int) {
      holder.itemBinding.tvQDescription.text = list[position].question
        holder.itemBinding.tvADescription.text = list[position].answer
    }

    override fun getItemCount(): Int {
       return list.size
    }

}