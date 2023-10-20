package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrosid.svgloader.SvgLoader
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.GoalsRecyclerRowBinding
import com.emilabdurahmanli.budgetmanager.model.Goals

class GoalsRecyclerAdapter(var list : List<Goals>, val listener: GoalsClickListener) : RecyclerView.Adapter<GoalsRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(var binding : GoalsRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GoalsRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == list.size){
            holder.binding.goalDetailLinearLayout.visibility = View.GONE
            holder.binding.oneImageConstraintLayout.visibility = View.VISIBLE
            holder.binding.detailImage.visibility = View.GONE
            holder.binding.addGoalImage.visibility = View.VISIBLE
            holder.binding.goalTitleTV.visibility = View.GONE
            holder.binding.goalSoldAmountTV.visibility = View.GONE
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.addGoalImage.setImageResource(R.drawable.goals_add)
            holder.binding.addGoalImage.setOnClickListener {
                listener.onClick(null)
            }
        }else{
            val totalAmount = list[position].totalAmount
            val payAmount = list[position].payAmount
            Log.d("Emilll", "total ${totalAmount}")
            Log.d("Emilll", "pay ${payAmount}")
            if(totalAmount!! == payAmount!!){
                Log.d("Emilll", "Work")
                holder.binding.oneImageConstraintLayout.visibility = View.VISIBLE
                holder.binding.addGoalImage.visibility = View.GONE
                holder.binding.detailImage.visibility = View.VISIBLE
                holder.binding.detailImage.setImageResource(R.drawable.goal_icon)
            }else{
                Log.d("Emilll", " Not Work")
                holder.binding.oneImageConstraintLayout.visibility = View.GONE
            }

            holder.binding.goalDetailLinearLayout.visibility = View.VISIBLE
            holder.binding.goalTitleTV.visibility = View.VISIBLE
            holder.binding.goalSoldAmountTV.visibility = View.VISIBLE
            holder.binding.progressBar.visibility = View.VISIBLE
            holder.binding.goalTitleTV.setText(list[position].goalTitle)
            holder.binding.goalSoldAmountTV.setText("Amount: ${list[position].totalAmount.toString()}")

            val progression = (payAmount/totalAmount) * 100

            holder.binding.progressBar.progress = progression.toInt()


            if(list[position].payAmount == 0.0){
                holder.binding.payIV.setImageResource(R.drawable.pay)
            }else{
                holder.binding.payIV.setImageResource(R.drawable.payed)
            }
            holder.binding.root.setOnClickListener {
                listener.onClick(list[position])
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun onDataChanged(list : List<Goals>){
        this.list = list
        notifyDataSetChanged()
    }
}

interface GoalsClickListener{
    fun onClick(goal: Goals?)
}