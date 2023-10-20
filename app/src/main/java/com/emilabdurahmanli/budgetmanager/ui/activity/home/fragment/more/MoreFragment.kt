package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentExpensesBinding
import com.emilabdurahmanli.budgetmanager.databinding.FragmentMoreBinding
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.emilabdurahmanli.budgetmanager.ui.activity.sign_in_log_in.LoginSignupActivity
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.more.MoreFragmentViewModel


class MoreFragment : Fragment() {


    private lateinit var binding: FragmentMoreBinding
    private lateinit var viewModel : MoreFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MoreFragmentViewModel::class.java]
        viewModel.getUserInfo()
        setUpObservers()
        setUpClickListeners()
    }

    private fun setUpObservers(){
        viewModel.observeUserInfo().observe(viewLifecycleOwner, Observer {
            binding.userNameSurnameTV.setText("${it.firstname} ${it.lastname}")
            binding.userMailTV.setText(it.email)
        })
    }
    private fun setUpClickListeners(){
        binding.incomeTV.setOnClickListener {
            loadFragment(IncomeFragment())
        }
        binding.signOutTV.setOnClickListener {
            FirebaseNetwork.auth.signOut()
            val intent = Intent(context, LoginSignupActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.container,fragment)
        transaction?.commit()
    }

}