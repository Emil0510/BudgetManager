package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.budgetmanager.databinding.IncomeRecyclerRowBinding
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.model.Transaction

class IncomeRecyclerAdapter(var list : List<Income>, var listener : IncomeOnclickListener) : RecyclerView.Adapter<IncomeRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: IncomeRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(IncomeRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.incomeAmountTV.setText(list[position].amount.toString())
        holder.binding.incomeTitleTV.setText(list[position].incomeTitle)
        holder.binding.incomeDateTV.setText(list[position].incomeDate)
        if (list[position].isCash!!){
            holder.binding.cashCardTextView.setText("Cash")
        }else{
            holder.binding.cashCardTextView.setText("Card")
        }
        holder.binding.root.setOnClickListener {
            listener.onClick(list[position])
        }
    }

    fun updateList(list : List<Income>){
        this.list = list
        notifyDataSetChanged()
    }
}

interface IncomeOnclickListener {
    fun onClick(income: Income)
}