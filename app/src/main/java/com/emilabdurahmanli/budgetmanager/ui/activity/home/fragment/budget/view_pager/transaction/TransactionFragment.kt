package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentTransactionBinding
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.adapter.TransactionOnCLickListener
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.adapter.TransactionRecyclerAdapter
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.BudgetViewModel


class TransactionFragment : Fragment() , TransactionOnCLickListener{


    private lateinit var binding : FragmentTransactionBinding
    private lateinit var list : List<Transaction>
    private lateinit var viewModel : BudgetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = listOf()
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        viewModel.getTransactions()



        binding.cardTransactionRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.cardTransactionRV.adapter =
            activity?.let { TransactionRecyclerAdapter(it, false,list, this ) }
        binding.cashTransactionRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.cashTransactionRV.adapter =
            activity?.let { TransactionRecyclerAdapter(it,false, list, this) }

        viewModel.observeCardTransactions().observe(viewLifecycleOwner, Observer {
            (binding.cardTransactionRV.adapter as TransactionRecyclerAdapter).updateList(it.reversed())
        })
        viewModel.observeCashTransactions().observe(viewLifecycleOwner, Observer {
            (binding.cashTransactionRV.adapter as TransactionRecyclerAdapter).updateList(it.reversed())
        })

        setUpClickListeners()
    }
    @SuppressLint("ResourceType")
    private fun setUpClickListeners(){
        binding.seeAllCardTransaction.setOnClickListener {
            loadFragment(AllTransactionFragment(), false)
        }
        binding.seeAllCashTransaction.setOnClickListener {
            loadFragment(AllTransactionFragment(), true)
        }
    }

    private  fun loadFragment(fragment: Fragment, isCash : Boolean){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putBoolean("isCash",isCash)
        transaction?.replace(R.id.container,fragment::class.java, bundle)
        transaction?.commit()
    }
    private  fun loadFragmentTransaction(fragment: Fragment, transactionDetail: Transaction){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("Transaction",transactionDetail)
        transaction?.replace(R.id.container,fragment::class.java, bundle)
        transaction?.commit()
    }

    override fun onClick(transaction: Transaction) {
        loadFragmentTransaction(TransactionDetailFragment(), transaction)
    }

}