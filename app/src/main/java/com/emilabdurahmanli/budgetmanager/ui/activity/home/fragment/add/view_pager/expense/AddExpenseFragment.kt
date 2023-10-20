package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.add.view_pager.expense

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.constants.Constant
import com.emilabdurahmanli.budgetmanager.databinding.FragmentAddExpenseBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.HomeActivity
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.dashboard.DashboardFragment
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.add.AddFragmentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddExpenseFragment : Fragment() {


    private lateinit var binding: FragmentAddExpenseBinding
    private lateinit var categoryArray: Array<ImageView>
    private var categoryImage: String = Constant.categoryImages.getValue("car")
    private var transactionCategory: String = "Transport"
    private var isCash: Boolean = true
    private lateinit var viewModel: AddFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddExpenseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddFragmentViewModel::class.java]
        viewModel.getBalance()

        binding.confirmButton.isEnabled = false
        binding.moreTitleET.visibility = View.GONE

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


        setUpObservers()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
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
        binding.carCategory.setOnClickListener {
            selectCategory(0)
            transactionCategory = "Transport"
            categoryImage = Constant.categoryImages.getValue("car")
            binding.moreTitleET.visibility = View.GONE
        }
        binding.beautyCategory.setOnClickListener {
            selectCategory(1)
            transactionCategory = "Beauty"
            categoryImage = Constant.categoryImages.getValue("beauty")
            binding.moreTitleET.visibility = View.GONE

        }
        binding.clothesCategory.setOnClickListener {
            selectCategory(2)
            transactionCategory = "Clothing"
            categoryImage = Constant.categoryImages.getValue("clothing")
            binding.moreTitleET.visibility = View.GONE
        }
        binding.foodCategory.setOnClickListener {
            selectCategory(3)
            transactionCategory = "Food"
            categoryImage = Constant.categoryImages.getValue("food")
            binding.moreTitleET.visibility = View.GONE
        }

        binding.donationCategory.setOnClickListener {
            selectCategory(4)
            transactionCategory = "Donation"
            categoryImage = Constant.categoryImages.getValue("donation")
            binding.moreTitleET.visibility = View.GONE
        }

        binding.healthCategory.setOnClickListener {
            selectCategory(5)
            transactionCategory = "Health"
            categoryImage = Constant.categoryImages.getValue("health")
            binding.moreTitleET.visibility = View.GONE
        }
        binding.homeCategory.setOnClickListener {
            selectCategory(6)
            transactionCategory = "Home"
            categoryImage = Constant.categoryImages.getValue("home")
            binding.moreTitleET.visibility = View.GONE
        }

        binding.moreCategory.setOnClickListener {
            selectCategory(7)
            transactionCategory = "More"
            categoryImage = Constant.categoryImages.getValue("more")
            binding.moreTitleET.visibility = View.VISIBLE
        }

        binding.presentCategory.setOnClickListener {
            selectCategory(8)
            transactionCategory = "Gift"
            categoryImage = Constant.categoryImages.getValue("gift")
            binding.moreTitleET.visibility = View.GONE
        }


        binding.confirmButton.setOnClickListener {
            if (binding.amountET.text.toString().trim().isNotEmpty()) {
                val sdf = SimpleDateFormat("yyyy MM dd | HH:mm:ss", Locale.getDefault())
                val currentDateAndTime: String = sdf.format(Date())
                viewModel.addExpense(
                    isCash,
                    categoryImage,
                    transactionCategory,
                    currentDateAndTime,
                    binding.amountET.text.toString().toDouble(),
                    binding.moreTitleET.text.toString()
                )
                binding.confirmButton.isEnabled = false
            } else {
                Toast.makeText(context, "Please enter amount", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setUpObservers() {
        viewModel.observeBalance().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                binding.confirmButton.isEnabled = true
            }
        })
        viewModel.observeExpenseOperation().observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                loadFragment(DashboardFragment())
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, it.operationMessage, Toast.LENGTH_LONG).show()

            }
        })
    }


    private fun selectCategory(position: Int) {

        for (i in categoryArray.indices) {
            if (i == position) {
                categoryArray[i].background =
                    resources.getDrawable(R.drawable.category_button_selected)
            } else {
                categoryArray[i].background = null
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        (activity as HomeActivity).selectBottom()

//        val transaction = activity?.supportFragmentManager?.beginTransaction()
//        transaction?.replace(R.id.container,fragment)
//        transaction?.commit()
    }

}