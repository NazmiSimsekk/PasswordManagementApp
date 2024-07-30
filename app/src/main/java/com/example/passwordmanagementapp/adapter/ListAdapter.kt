package com.example.passwordmanagementapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanagementapp.databinding.PasswordListBinding
import com.example.passwordmanagementapp.model.PassModel

class ListAdapter(val passList: ArrayList<PassModel>): RecyclerView.Adapter<ListAdapter.PassViewHolder>(){

    class PassViewHolder(var binding: PasswordListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PassViewHolder, position: Int) {
        holder.binding.passwordListXml = passList[position]
    }

    override fun getItemCount(): Int {
        return passList.size
    }
}