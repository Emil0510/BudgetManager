package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.add.view_pager.income

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentAddIncomeBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.HomeActivity
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.dashboard.DashboardFragment
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.add.AddFragmentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddIncomeFragment : Fragment() {

    private lateinit var binding: FragmentAddIncomeBinding
    private lateinit var viewModel: AddFragmentViewModel
    private var isCash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddIncomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddFragmentViewModel::class.java]
        viewModel.getBalance()
        binding.confirmButton.isEnabled = false
        setOnClickListeners()
        setObservers()

    }


    fun setOnClickListeners() {
        binding.cashAccountButton.setOnClickListener {
            isCash = true
            binding.cashAccountButton.setText("✓ Cash")
            binding.cardAccountButton.setText("Card")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cashAccountButton.setTextColor(Color.WHITE)
            binding.cardAccountButton.setTextColor(Color.BLACK)
        }
        binding.cardAccountButton.setOnClickListener {
            isCash = false
            binding.cardAccountButton.setText("✓ Card")
            binding.cashAccountButton.setText("Cash")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cashAccountButton.setTextColor(Color.BLACK)
            binding.cardAccountButton.setTextColor(Color.WHITE)
        }

        binding.confirmButton.setOnClickListener {
            if (binding.amountET.text.toString().trim().isNotEmpty()) {
                val sdf = SimpleDateFormat("yyyy MM dd | HH:mm:ss", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())
                viewModel.addIncome(
                    binding.amountET.text.toString().toDouble(),
                    isCash,
                    binding.incomeTitleET.text.toString(),
                    currentDateAndTime
                )
                binding.confirmButton.isEnabled = false
            } else {
                Toast.makeText(context, "Please enter amount", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setObservers() {
        viewModel.observeBalance().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                binding.confirmButton.isEnabled = true
            }
        })
        viewModel.observeIncomeOperation().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                loadFragment(DashboardFragment())
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()

            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        (activity as HomeActivity).selectBottom()
    }

}