package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.expense.ExpensesFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals.GoalsFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.TransactionFragment

class BudgetViewPagerAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return TransactionFragment()
            }
            1 -> {
                return ExpensesFragment()
            }
            2 -> {
                return GoalsFragment()
            }
            else -> {
                return TransactionFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Transactions"
            }
            1 -> {
                return "Expenses"
            }
            2 -> {
                return "Goals"
            }
        }
        return super.getPageTitle(position)
    }

}