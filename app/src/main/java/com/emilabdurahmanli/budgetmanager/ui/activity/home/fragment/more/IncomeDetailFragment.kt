package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentIncomeDetailBinding
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.more.MoreFragmentViewModel


class IncomeDetailFragment : Fragment() {

    private lateinit var binding : FragmentIncomeDetailBinding
    private lateinit var income : Income
    private lateinit var viewModel : MoreFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncomeDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MoreFragmentViewModel::class.java]
        viewModel.getBalance()
        binding.deleteIncomeButton.isEnabled = false
        income = arguments?.getSerializable("Income") as Income
        binding.incomeAmonntET.setText(income.amount.toString())
        binding.incomeTitleEditText.setText(income.incomeTitle)
        if (income.isCash == true){
            binding.cashAccountButton.setText("✓ Cash")
            binding.cardAccountButton.setText("Card")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cashAccountButton.setTextColor(R.color.white)
            binding.cardAccountButton.setTextColor(R.color.black)
        }else{
            binding.cardAccountButton.setText("✓ Card")
            binding.cashAccountButton.setText("Cash")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cashAccountButton.setTextColor(R.color.black)
            binding.cardAccountButton.setTextColor(R.color.white)
        }

        binding.include.fragmentNameText.visibility = View.INVISIBLE

        setUpObservers()
        setUpClickListeners()
        setUnEnabled()
    }

    private fun setUpObservers(){
        viewModel.observeBalanceOperation().observe(viewLifecycleOwner, Observer {
            if(it.isSuccessful){
                binding.deleteIncomeButton.isEnabled = true
            }
        })

        viewModel.observeDeleteOperation().observe(viewLifecycleOwner, Observer {
            if(it.isSuccessful){
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
                loadFragment(IncomeFragment())
            }else{
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun setUpClickListeners(){
        binding.include.backArrowIV.setOnClickListener {
            loadFragment(IncomeFragment())
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            loadFragment(IncomeFragment())
        }
        binding.deleteIncomeButton.setOnClickListener {
            viewModel.deleteIncome(income.amount!!, income.isCash!!, income.incomeDate!!)
        }
    }

    private fun setUnEnabled(){
        binding.incomeAmonntET.isEnabled = false
        binding.cardAccountButton.isEnabled = false
        binding.cashAccountButton.isEnabled = false
        binding.incomeTitleEditText.isEnabled = false
        binding.editButton.visibility = View.GONE
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.container, fragment)
        transaction?.commit()
    }


}