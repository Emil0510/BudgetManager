package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.advice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.budgetmanager.databinding.AdviceRecyclerRowBinding

class AdvicesRecyclerViewAdapter(var list : List<Int>, var listener : AdviceOnClickListener) : RecyclerView.Adapter<AdvicesRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : AdviceRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdviceRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageView13.setImageResource(list[position])
        holder.binding.root.setOnClickListener {
            listener.onClick()
        }
    }

}

interface AdviceOnClickListener{
    fun onClick()
}