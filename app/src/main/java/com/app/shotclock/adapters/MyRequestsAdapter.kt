package com.app.shotclock.adapters

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsMyrequestsBinding

class MyRequestsAdapter : RecyclerView.Adapter<MyRequestsAdapter.MyRequestHolder>() {

    class MyRequestHolder(itemsView: ItemsMyrequestsBinding) :
        RecyclerView.ViewHolder(itemsView.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsMyrequestsBinding.inflate(inflater, parent, false)
        return MyRequestHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRequestHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }
}