package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentTransactionDetailBinding
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.BudgetViewModel


class TransactionDetailFragment : Fragment() {


    private lateinit var binding: FragmentTransactionDetailBinding
    private lateinit var transaction: Transaction
    private lateinit var viewModel: BudgetViewModel
    private lateinit var categoryArray: Array<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        binding.deleteTransactionButton.isEnabled = false
        transaction = arguments?.getSerializable("Transaction") as Transaction
        viewModel.getBalance(transaction.isGoal!!, transaction.goalDate!!)
        if(transaction.isGoal!!){
            binding.transactionConstraintLayout.visibility = View.GONE
            binding.goalConstraintLayout.visibility = View.VISIBLE
            binding.moreTitleET.visibility = View.GONE
            binding.goalTitleTV.setText(transaction.goalTitle)

        }else{
            categoryArray = arrayOf<ImageView>(
                binding.carCategory,
                binding.beautyCategory,
                binding.clothesCategory,
                binding.foodCategory,
                binding.donationCategory,
                binding.healthCategory,
                binding.homeCategory,
                binding.moreCategory,
                binding.presentCategory
            )
            setUpUI()
        }

        binding.transactionAmountTV.setText(transaction.transactionAmount.toString())
        binding.transactionDateTV.setText(transaction.transactionDate)

        if(transaction.isCash==true){
            binding.cashAccountButton.setText("✓ Cash")
            binding.cardAccountButton.setText("Card")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cashAccountButton.setTextColor(Color.WHITE)
            binding.cardAccountButton.setTextColor(Color.BLACK)
        }else{
            binding.cardAccountButton.setText("✓ Card")
            binding.cashAccountButton.setText("Cash")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cashAccountButton.setTextColor(Color.BLACK)
            binding.cardAccountButton.setTextColor(Color.WHITE)
        }


        binding.include.fragmentNameText.visibility = View.INVISIBLE
        binding.editButton.visibility = View.GONE
        binding.moreTitleET.visibility = View.GONE

        setUpClickListeners()
        setUpObservers()
        setUnEnabled()
    }

    private fun setUpUI(){

        when(transaction.transactionCategory){
            "Transport" -> {selectCategory(0)}
            "Beauty" -> {selectCategory(1) }
            "Clothing" -> {selectCategory(2)}
            "Food" -> {selectCategory(3)}
            "Donation" -> {selectCategory(4)}
            "Health" -> {selectCategory(5)}
            "Home" -> {selectCategory(6)}
            "More" -> {selectCategory(7)
            binding.moreTitleET.visibility = View.VISIBLE
            binding.moreTitleET.setText(transaction.moreTransactionTitle)}
            "Gift" -> {selectCategory(8)}
        }

    }

    private fun setUnEnabled(){
        binding.transactionAmountTV.isEnabled = false
        binding.transactionDateTV.isEnabled = false
        binding.moreTitleET.isEnabled = false
    }
    private fun setUpClickListeners() {
        binding.include.backArrowIV.setOnClickListener {
            transaction.isCash?.let { it1 -> loadFragment(AllTransactionFragment(), it1) }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            transaction.isCash?.let { it1 -> loadFragment(AllTransactionFragment(), it1) }
        }
        binding.deleteTransactionButton.setOnClickListener {
            viewModel.delete(
                transaction.transactionAmount!!, transaction.isCash!!,
                transaction.transactionDate!!,
                transaction.isGoal!!,
                transaction.goalDate!!
            )
        }
    }

    private fun selectCategory(position: Int) {

        for (i in categoryArray.indices) {
            if (i == position) {
                categoryArray[i].background =
                    resources.getDrawable(R.drawable.category_button_selected)
            } else {
                categoryArray[i].background = null
            }
            categoryArray[i].isEnabled = false
        }
    }

    private fun setUpObservers() {
        viewModel.observeBalanceOperation().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                binding.deleteTransactionButton.isEnabled = true
            }
        })

        viewModel.observeDeleteOperation().observe(viewLifecycleOwner, Observer {
            if(it.isSuccessful){
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
                transaction.isCash?.let { it1 -> loadFragment(AllTransactionFragment(), it1) }
            }else{
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadFragment(fragment: Fragment, isCash: Boolean) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        val bundle = Bundle()
        bundle.putBoolean("isCash", isCash)
        transaction?.replace(R.id.container, fragment::class.java, bundle)
        transaction?.commit()
    }

}