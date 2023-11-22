package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.LinearLayoutManager
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentIncomeBinding
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more.adapter.IncomeOnclickListener
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more.adapter.IncomeRecyclerAdapter
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.more.MoreFragmentViewModel


class IncomeFragment : Fragment() , IncomeOnclickListener{


    private lateinit var binding : FragmentIncomeBinding
    private lateinit var list : List<Income>
    private lateinit var viewModel : MoreFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MoreFragmentViewModel::class.java]
        list = listOf()

        viewModel.getIncomes()
        binding.include2.fragmentNameText.setText("Income")
        binding.include2.backArrowIV.setOnClickListener {
            loadFragment(MoreFragment())
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            loadFragment(MoreFragment())
        }
        binding.incomeRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.incomeRV.adapter = IncomeRecyclerAdapter(list, this)

        setUpObservers()

    }
    fun setUpObservers(){
        viewModel.observeIncomes().observe(viewLifecycleOwner, Observer {
            (binding.incomeRV.adapter as IncomeRecyclerAdapter).updateList(it.reversed())
        })
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.container,fragment)
        transaction?.commit()
    }

    private  fun loadFragmentIncome(fragment: Fragment, incomeDetail: Income){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("Income",incomeDetail)
        transaction?.replace(R.id.container,fragment::class.java, bundle)
        transaction?.commit()
    }

    override fun onClick(income: Income) {
        loadFragmentIncome(IncomeDetailFragment(), income)
    }


}