package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentBudgetBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.adapter.BudgetViewPagerAdapter
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.BudgetViewModel

class BudgetFragment : Fragment() {

    private lateinit var binding : FragmentBudgetBinding
    private lateinit var viewModel : BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBudgetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        viewModel.getBalance(false, "")

        binding.include.fragmentNameText.setText("Budget")
        binding.include.backArrowIV.visibility = View.GONE

        binding.budgetViewPager.adapter = BudgetViewPagerAdapter(childFragmentManager)
        binding.budgetTabLayout.setupWithViewPager(binding.budgetViewPager)


        setUpObservers()


    }
    private fun setUpObservers(){
        viewModel.observeBalance().observe(viewLifecycleOwner, Observer {
            binding.totalBalanceTV.setText("${it.card?.plus(it.cash!!)}")
            binding.cashTV.setText("${it.cash}")
            binding.cardTV.setText("${it.card}")
        })
    }


}