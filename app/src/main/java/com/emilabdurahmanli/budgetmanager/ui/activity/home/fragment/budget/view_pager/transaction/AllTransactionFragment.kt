package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentAllTransactionBinding
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.BudgetFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.adapter.TransactionOnCLickListener
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.adapter.TransactionRecyclerAdapter
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.dashboard.DashboardFragment
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.BudgetViewModel


class AllTransactionFragment : Fragment(), TransactionOnCLickListener {


    private lateinit var binding: FragmentAllTransactionBinding
    private lateinit var list: MutableList<Transaction>
    private lateinit var viewModel: BudgetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        viewModel.getTransactions()
        list = mutableListOf()

        val isCash = arguments?.getBoolean("isCash")

        binding.include.backArrowIV.setOnClickListener {
            loadFragment(BudgetFragment())
        }

        binding.allTransactionRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.allTransactionRV.adapter =
            activity?.let { TransactionRecyclerAdapter(it, true, list, this) }


        if (isCash == true) {
            binding.include.fragmentNameText.text = "Cash"
            viewModel.observeCashTransactions().observe(viewLifecycleOwner, Observer {
                (binding.allTransactionRV.adapter as TransactionRecyclerAdapter).updateList(it.reversed())
            })

        } else {
            binding.include.fragmentNameText.text = "Card"
            viewModel.observeCardTransactions().observe(viewLifecycleOwner, Observer {
                (binding.allTransactionRV.adapter as TransactionRecyclerAdapter).updateList(it.reversed())
            })
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.container, fragment)
        transaction?.commit()
    }

    private fun loadFragmentTransaction(fragment: Fragment, transactionDetail: Transaction) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("Transaction", transactionDetail)
        transaction?.replace(R.id.container, fragment::class.java, bundle)
        transaction?.commit()
    }

    override fun onClick(transaction: Transaction) {
        loadFragmentTransaction(TransactionDetailFragment(), transaction)
    }

}