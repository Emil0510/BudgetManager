package com.emilabdurahmanli.budgetmanager.ui.activity.sign_in_log_in.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentSignInBinding
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.emilabdurahmanli.budgetmanager.ui.activity.home.HomeActivity
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.sign_in_log_up.SignInSignUpViewModel

class SignInFragment : Fragment() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding : FragmentSignInBinding
    private lateinit var viewModel : SignInSignUpViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences("Data", Context.MODE_PRIVATE)!!
        viewModel = ViewModelProvider(this)[SignInSignUpViewModel::class.java]
        fragmentManager = requireFragmentManager()
        val isEnteredBalance = sharedPreferences.getBoolean("isEnteredBalance", true)
        if(FirebaseNetwork.auth.currentUser!=null){
            //Intent to home activity
            if(isEnteredBalance){
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(context, "You are not signed up till the end", Toast.LENGTH_LONG).show()
                Toast.makeText(context, "Please enter balance", Toast.LENGTH_LONG).show()
                loadFragment(FirstSignUpFragment())
            }
        }

        viewModel.observeSignInMessage().observe(viewLifecycleOwner, Observer {operation ->
            if(operation.isSuccessful){
                Toast.makeText(context, operation.operationMessage, Toast.LENGTH_LONG).show()
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
                sharedPreferences.edit().putBoolean("isFirst", false).apply()
                activity?.finish()
            }else{
                Toast.makeText(context, operation.operationMessage, Toast.LENGTH_LONG).show()
            }
        })
        binding.signUpIV.setOnClickListener {
            loadFragment(SignUpFragment())
        }
        binding.signInButton.setOnClickListener {
            if(checkInput()){
                viewModel.signIn(binding.signInEmailET.text.toString(), binding.signInPasswordET.text.toString())
            }else{
                Toast.makeText(context, "Please enter password and email", Toast.LENGTH_LONG).show()
            }
        }

    }
    private fun checkInput(): Boolean{
        return binding.signInEmailET.text.toString().trim().isNotEmpty() && binding.signInPasswordET.text?.trim()!!.isNotEmpty()
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.LogInSignUpFragmentContainerView,fragment)
        transaction.commit()
    }


}