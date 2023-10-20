package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.add.view_pager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.add.view_pager.expense.AddExpenseFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.add.view_pager.income.AddIncomeFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.expense.ExpensesFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.goals.GoalsFragment
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.transaction.TransactionFragment

class AddViewPagerAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return AddIncomeFragment()
            }
            1 -> {
                return AddExpenseFragment()
            }
            else -> {
                return AddIncomeFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "New income"
            }
            1 -> {
                return "New expense"
            }
        }
        return super.getPageTitle(position)
    }

}