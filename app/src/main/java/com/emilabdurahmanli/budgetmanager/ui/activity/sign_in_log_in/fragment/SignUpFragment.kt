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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentSignUpBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.HomeActivity
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.sign_in_log_up.SignInSignUpViewModel


class SignUpFragment : Fragment() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignInSignUpViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences("Data", Context.MODE_PRIVATE)!!
        fragmentManager = requireFragmentManager()
        viewModel = ViewModelProvider(this)[SignInSignUpViewModel::class.java]
        binding.signUpButton.setOnClickListener {

            if(checkInput()){
                viewModel.signUp(
                    binding.signUpEmailET.text.toString(),
                    binding.signUpPasswordET.text.toString(),
                    binding.signUpFirstNameET.text.toString(),
                    binding.signUpLastNameET.text.toString()
                )
            }else {
                Toast.makeText(context, "You entered something wrong or even not entered", Toast.LENGTH_LONG).show()
            }
        }


        viewModel.observeSignUpMessage().observe(viewLifecycleOwner, Observer { operation ->
            if (operation.isSuccessful){
                Toast.makeText(context, operation.operationMessage, Toast.LENGTH_LONG).show()
                sharedPreferences.edit().putBoolean("isFirst", false).apply()
                sharedPreferences.edit().putBoolean("isEnteredBalance", false).apply()
                loadFragment(FirstSignUpFragment())
            }else{
                Toast.makeText(context, operation.operationMessage, Toast.LENGTH_LONG).show()
            }
        })

        binding.signInIV.setOnClickListener {
            loadFragment(SignInFragment())
        }

    }

    fun checkInput(): Boolean {
        return binding.signUpFirstNameET.text.toString().trim().isNotEmpty() && binding.signUpLastNameET.text.toString().trim()
            .isNotEmpty() && binding.signUpEmailET.text.toString().trim()
            .isNotEmpty() && binding.signUpPasswordET.text.toString().trim().length >= 8
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.LogInSignUpFragmentContainerView, fragment)
        transaction.commit()
    }

}