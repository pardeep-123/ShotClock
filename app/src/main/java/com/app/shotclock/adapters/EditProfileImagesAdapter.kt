package com.app.shotclock.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.shotclock.databinding.ItemsCompleteProfileImagesBinding

class EditProfileImagesAdapter : RecyclerView.Adapter<EditProfileImagesAdapter.EditHolder>(){

    class EditHolder(itemsView : ItemsCompleteProfileImagesBinding) : RecyclerView.ViewHolder(itemsView.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemsCompleteProfileImagesBinding.inflate(inflater,parent,false)
        return EditHolder(binding)
    }

    override fun onBindViewHolder(holder: EditHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }
}