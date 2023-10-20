package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrosid.svgloader.SvgLoader
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.TransactionRecyclerRowBinding
import com.emilabdurahmanli.budgetmanager.model.Transaction

class TransactionRecyclerAdapter(var activity : FragmentActivity, val isAll : Boolean, var list : List<Transaction>, var listener : TransactionOnCLickListener) : RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding : TransactionRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(TransactionRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        if(isAll){
            return list.size
        }else{
            if(list.size<=4){
                return list.size
            }else{
                return 4
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        SvgLoader.pluck()
            .with(activity)
            .load(list[position].categoryImage, holder.binding.categoryImage)
        if (list[position].transactionCategory.equals("More")){
            holder.binding.transactionCategoryRVTV.setText(list[position].moreTransactionTitle)
        }else{
            holder.binding.transactionCategoryRVTV.setText(list[position].transactionCategory)
        }
        holder.binding.transactionAmountRVTV.setText(list[position].transactionAmount.toString())
        holder.binding.transactionDateRVTV.setText(list[position].transactionDate)
        holder.binding.root.setOnClickListener {
            //To Transaction Detail Fragment
            listener.onClick(list[position])
        }
    }

    fun updateList(list : List<Transaction>){
        this.list = list
        notifyDataSetChanged()
    }

}

interface TransactionOnCLickListener {
    fun onClick(transaction: Transaction)
}