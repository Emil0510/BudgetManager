package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.budget.view_pager.expense

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentExpensesBinding
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget.BudgetViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF


class ExpensesFragment : Fragment() {


    private lateinit var binding : FragmentExpensesBinding
    private lateinit var barData: BarData
    private lateinit var barDataSet: BarDataSet
    private lateinit var barEntriesList: ArrayList<BarEntry>
    private lateinit var viewModel : BudgetViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpensesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BudgetViewModel::class.java]
        viewModel.getExpenses()
        viewModel.observeTopTransaction().observe(viewLifecycleOwner, Observer {
            setExpensePieChart(it)
        })

        viewModel.observeListOfAllTransactions().observe(viewLifecycleOwner, Observer {
            setBarChart(it)
        })


    }
    private fun setBarChart(list : List<Transaction>){
        getBarChartData(list)
        // on below line we are initializing our bar data set
        barDataSet = BarDataSet(barEntriesList, "Categories of expenses")

        // on below line we are initializing our bar data
        barData = BarData(barDataSet)

        // on below line we are setting data to our bar chart
        binding.expenseBarChar.data = barData

        // on below line we are setting colors for our bar chart text
        barDataSet.valueTextColor = Color.BLACK

        // on below line we are setting color for our bar data set
        barDataSet.setColor(resources.getColor(R.color.purple_200))

        // on below line we are setting text size
        barDataSet.valueTextSize = 10f


        // on below line we are enabling description as false
        binding.expenseBarChar.description.isEnabled = false
    }

    private fun  setExpensePieChart(transactions : List<Transaction>){
        if(transactions.size == 1){
            binding.expenseGreenTV.setText(transactions[0].transactionCategory)
        }else if (transactions.size == 2){
            binding.expenseGreenTV.setText(transactions[0].transactionCategory)
            binding.expenseBlueTV.setText(transactions[1].transactionCategory)
        }


        binding.expensePieChart.setUsePercentValues(false)
        binding.expensePieChart.getDescription().setEnabled(false)
        binding.expensePieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.expensePieChart.setDragDecelerationFrictionCoef(0.95f)

        binding.expensePieChart.setDrawHoleEnabled(false)
        binding.expensePieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        binding.expensePieChart.setTransparentCircleColor(Color.WHITE)
        binding.expensePieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        binding.expensePieChart.setHoleRadius(58f)
        binding.expensePieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        binding.expensePieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        binding.expensePieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        binding.expensePieChart.setRotationEnabled(true)
        binding.expensePieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        binding.expensePieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        binding.expensePieChart.legend.isEnabled = false
        binding.expensePieChart.setEntryLabelColor(Color.WHITE)
        binding.expensePieChart.setEntryLabelTextSize(8f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()

        if(transactions.size==1){
            entries.add(PieEntry(transactions[0].transactionAmount!!.toFloat(), "Azn"))
        }else if(transactions.size==2){
            entries.add(PieEntry(transactions[0].transactionAmount!!.toFloat(), "Azn"))
            entries.add(PieEntry(transactions[1].transactionAmount!!.toFloat(), "Azn"))
        }else if(transactions.isEmpty()){
            entries.add(PieEntry(0f, "Azn"))
            entries.add(PieEntry(0f, "Azn"))
        }

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.open_green2))
        colors.add(resources.getColor(R.color.blue))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(12f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.expensePieChart.setData(data)

        // undo all highlights
        binding.expensePieChart.highlightValues(null)

        // loading chart
        binding.expensePieChart.invalidate()
    }

    private fun getBarChartData(list : List<Transaction>) {

        barEntriesList = ArrayList()
        // on below line we are adding data
        // to our bar entries list
        var x  = 2f

        for (i in list.indices ){
            when(i){
                0 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))} //car
                1-> {barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}// beauty
                2 -> {barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//clothing
                3 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//food
                4 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//donation
                5 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//health
                6 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//hom
                7 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//more
                8 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//gift
                9 ->{barEntriesList.add(BarEntry(x, list[i].transactionAmount!!.toFloat()))}//goal
            }

            x+=2f
        }

    }


}