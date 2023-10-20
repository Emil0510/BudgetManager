package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.emilabdurahmanli.budgetmanager.databinding.FragmentGoalsBinding
import com.emilabdurahmanli.budgetmanager.model.Goals
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals.adapter.GoalsClickListener
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals.adapter.GoalsRecyclerAdapter
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.GoalsViewModel


class GoalsFragment : Fragment(), GoalsClickListener {


    private lateinit var binding: FragmentGoalsBinding
    private lateinit var goalList : List<Goals>
    private lateinit var viewModel : GoalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGoalsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GoalsViewModel::class.java]
        viewModel.getGoals()
        goalList = listOf()
        binding.goalsRV.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        binding.goalsRV.adapter = GoalsRecyclerAdapter(goalList, this)
        setUpObservers()
    }
    private fun setUpObservers(){
        viewModel.observeGoals().observe(viewLifecycleOwner, Observer {
            (binding.goalsRV.adapter as GoalsRecyclerAdapter).onDataChanged(it)
        })
    }

    override fun onClick(goal: Goals?) {
        val bottomSheet = AddGoalFragment()
        if(goal==null){
            activity?.let { bottomSheet.show(it.supportFragmentManager, "Add Goal") }
        }else{
            //Show goals with data
            val bundle = Bundle()
            bundle.putSerializable("Goal", goal)
            bottomSheet.arguments = bundle
            activity?.let { bottomSheet.show(it.supportFragmentManager, "Add Goal") }
        }
    }

}