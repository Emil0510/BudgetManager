package com.emilabdurahmanli.budgetmanager.ui.activity.sign_in_log_in.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.ActivityLoginSignupBinding
import com.emilabdurahmanli.budgetmanager.databinding.FragmentFirstSignUpBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.HomeActivity
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.sign_in_log_up.SignInSignUpViewModel


class FirstSignUpFragment : Fragment() {

    private lateinit var binding: FragmentFirstSignUpBinding
    private lateinit var viewModel : SignInSignUpViewModel
    private lateinit var sharedPreferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFirstSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SignInSignUpViewModel::class.java]
        sharedPreferences = context?.getSharedPreferences("Data", Context.MODE_PRIVATE)!!
        binding.firstSignNextButton.setOnClickListener{
            if(binding.firstSignCashET.text.toString().trim().isNotEmpty() && binding.firstSignCardET.text.toString().trim().isNotEmpty()){
                viewModel.setBalance(binding.firstSignCashET.text.toString().toDouble(),binding.firstSignCardET.text.toString().toDouble())
            }else{
                Toast.makeText(context, "Please enter balance", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.observeBalanceOperationMessage().observe(viewLifecycleOwner, Observer { operation ->
            if(operation.isSuccessful){
                Toast.makeText(context, operation.operationMessage, Toast.LENGTH_LONG).show()
                val intent = Intent(context, HomeActivity::class.java)
                sharedPreferences.edit().putBoolean("isEnteredBalance", true).apply()
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(context, operation.operationMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

}