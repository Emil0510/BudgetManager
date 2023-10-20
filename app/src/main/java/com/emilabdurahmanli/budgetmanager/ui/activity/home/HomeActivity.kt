package com.emilabdurahmanli.budgetmanager.ui.activity.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.ActivityHomeBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.add.AddFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.advice.AdvicesFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.BudgetFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.dashboard.DashboardFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.more.MoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {


    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(DashboardFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when (it) {
                0 -> {
                    loadFragment(DashboardFragment())
                    true
                }
                1 -> {
                    loadFragment(BudgetFragment())
                    true
                }
                2 -> {
                    loadFragment(AddFragment())
                    true
                }
                3 -> {
                    loadFragment(AdvicesFragment())
                    true
                }
                4 -> {
                    loadFragment(MoreFragment())
                    true
                }

                else -> {true}
            }
        }

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
    fun selectBottom(){
        loadFragment(DashboardFragment())
        binding.bottomNav.itemActiveIndex = 0
    }

}