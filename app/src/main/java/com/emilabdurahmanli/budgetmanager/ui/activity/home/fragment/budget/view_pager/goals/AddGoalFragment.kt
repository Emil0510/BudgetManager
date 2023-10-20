package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentAddGoalBinding
import com.emilabdurahmanli.budgetmanager.model.Goals
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.GoalsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddGoalFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddGoalBinding
    private lateinit var viewModel : GoalsViewModel
    private lateinit var goal : Goals
    private  var isCash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddGoalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GoalsViewModel::class.java]
        setUpClickListeners()
        setUpObservers()
        if(arguments?.getSerializable("Goal")!=null){
            binding.payButton.isEnabled = false
            viewModel.getBalance()
            goal = arguments?.getSerializable("Goal") as Goals
            binding.addButton.visibility = View.GONE
            binding.goalTitleET.isEnabled = false
            binding.goalAmountET.isEnabled = false
            binding.payedAmountET.isEnabled = false
            binding.leftAmountET.isEnabled = false
            binding.goalTitleET.setText(goal.goalTitle)
            binding.goalAmountET.setText("Total: ${goal.totalAmount.toString()}")
            binding.payedAmountET.setText("Payed: ${goal.payAmount.toString()}")
            binding.leftAmountET.setText("Left: ${goal.totalAmount?.minus(goal.payAmount!!).toString()}")
        }else{
            binding.goalDeleteButton.visibility = View.GONE
            binding.payButton.visibility = View.GONE
            binding.payCardView.visibility = View.GONE
            binding.leftCardView.visibility = View.GONE
            binding.payedCardView.visibility = View.GONE
            binding.textView7.visibility = View.GONE
            binding.cashAccountButton.visibility = View.GONE
            binding.cardAccountButton.visibility = View.GONE
        }
    }

    private fun setUpClickListeners(){
        binding.addButton.setOnClickListener {
            if(binding.goalAmountET.text.toString().trim().isNotEmpty() && binding.goalTitleET.text.toString().trim().isNotEmpty()){
                val sdf = SimpleDateFormat("yyyy MM dd | HH:mm:ss", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())
                viewModel.addGoals(currentDateAndTime, binding.goalTitleET.text.toString(), binding.goalAmountET.text.toString().toDouble())
            }else{
                Toast.makeText(context, "Please enter title and amount", Toast.LENGTH_LONG).show()
            }
        }

        binding.cashAccountButton.setOnClickListener {
            binding.cashAccountButton.setText("✓ Cash")
            binding.cardAccountButton.setText("Card")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cashAccountButton.setTextColor(Color.WHITE)
            binding.cardAccountButton.setTextColor(Color.BLACK)
            isCash = true
        }
        binding.cardAccountButton.setOnClickListener {
            binding.cardAccountButton.setText("✓ Card")
            binding.cashAccountButton.setText("Cash")
            binding.cashAccountButton.background =
                resources.getDrawable(R.drawable.account_button_not_selected)
            binding.cardAccountButton.background =
                resources.getDrawable(R.drawable.account_button_selected)
            binding.cashAccountButton.setTextColor(Color.BLACK)
            binding.cardAccountButton.setTextColor(Color.WHITE)
            isCash = false
        }

        binding.payButton.setOnClickListener {
            if(binding.payAmountET.text.toString().toDouble() > goal.totalAmount?.minus(goal.payAmount!!)!!){
                Toast.makeText(context, "You can pay maximum ${goal.totalAmount?.minus(goal.payAmount!!).toString()} amount", Toast.LENGTH_LONG).show()
            }else{
                val sdf = SimpleDateFormat("yyyy MM dd | HH:mm:ss", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())
                viewModel.payGoal(goal.goalDate.toString(), binding.payAmountET.text.toString().toDouble(),currentDateAndTime, isCash, goal.goalTitle.toString(), goal.payAmount!! )
            }
        }

        binding.goalDeleteButton.setOnClickListener {
            viewModel.deleteGoal(goal.goalDate.toString())
        }
    }
    private fun setUpObservers(){
        viewModel.observeAddGoalOperation().observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            if(it.isSuccessful){
                dismiss()
            }
        })

        viewModel.observeGetBalanceOperation().observe(viewLifecycleOwner, Observer {
            if(it.isSuccessful){
                binding.payButton.isEnabled = true
            }else{
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.observePayOperation().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful){
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
                dismiss()
            }else{
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.observeDeleteOperation().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful){
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
                dismiss()
            }else{
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            }
        })


    }

}