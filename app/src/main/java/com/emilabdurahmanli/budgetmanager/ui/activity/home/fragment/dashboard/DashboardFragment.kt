package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.dashboard

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentDashboardBinding
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.dashboard.DashboardFragmentViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF


class DashboardFragment : Fragment() {

    private lateinit var binding : FragmentDashboardBinding
    private lateinit var barData: BarData
    private lateinit var barDataSet: BarDataSet
    private lateinit var barEntriesList: ArrayList<BarEntry>
    private lateinit var viewModel : DashboardFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DashboardFragmentViewModel::class.java]
        viewModel.getBalance()
        viewModel.getExpenses()
        viewModel.getIncomes()

        binding.include.backArrowIV.visibility = View.GONE
        binding.include.fragmentNameText.text = "Dashboard"

        viewModel.observeBalance().observe(viewLifecycleOwner, Observer { balance ->
            setBalancePieChart(balance)
        })

        viewModel.observeTwoExpense().observe(viewLifecycleOwner, Observer {
            setExpensePieChart(it)
        })

        viewModel.observeIncomes().observe(viewLifecycleOwner, Observer {
            setIncomePieChart(it)
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
    private fun setBalancePieChart(balance: Balance){

        val total = balance.card?.plus(balance.cash!!)
        binding.totlaBalanceET.setText("${total}")
        binding.cashBalanceET.setText("Cash: ${balance.cash}")
        binding.cardBalanceET.setText("Card: ${balance.card}")

        binding.balancePieChart.setUsePercentValues(false)
        binding.balancePieChart.getDescription().setEnabled(false)
        binding.balancePieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.balancePieChart.setDragDecelerationFrictionCoef(0.95f)

        binding.balancePieChart.setDrawHoleEnabled(false)
        binding.balancePieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        binding.balancePieChart.setTransparentCircleColor(Color.WHITE)
        binding.balancePieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        binding.balancePieChart.setHoleRadius(58f)
        binding.balancePieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        binding.balancePieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        binding.balancePieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        binding.balancePieChart.setRotationEnabled(true)
        binding.balancePieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        binding.balancePieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        binding.balancePieChart.legend.isEnabled = false
        binding.balancePieChart.setEntryLabelColor(Color.WHITE)
        binding.balancePieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(balance.cash?.toFloat()!!, "Azn"))
        entries.add(PieEntry(balance.card?.toFloat()!!, "Azn"))

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
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.balancePieChart.setData(data)

        // undo all highlights
        binding.balancePieChart.highlightValues(null)

        // loading chart
        binding.balancePieChart.invalidate()

    }
    private fun setIncomePieChart(incomes : List<Income>){
        binding.incomePieChart.setUsePercentValues(false)
        binding.incomePieChart.getDescription().setEnabled(false)
        binding.incomePieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.incomePieChart.setDragDecelerationFrictionCoef(0.95f)

        binding.incomePieChart.setDrawHoleEnabled(false)
        binding.incomePieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        binding.incomePieChart.setTransparentCircleColor(Color.WHITE)
        binding.incomePieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        binding.incomePieChart.setHoleRadius(58f)
        binding.incomePieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        binding.incomePieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        binding.incomePieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        binding.incomePieChart.setRotationEnabled(true)
        binding.incomePieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        binding.incomePieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        binding.incomePieChart.legend.isEnabled = false
        binding.incomePieChart.setEntryLabelColor(Color.WHITE)
        binding.incomePieChart.setEntryLabelTextSize(8f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()



        entries.add(PieEntry(incomes[0].amount?.toFloat()!!, "Azn"))
        entries.add(PieEntry(incomes[1].amount?.toFloat()!!, "Azn"))


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
        binding.incomePieChart.setData(data)

        // undo all highlights
        binding.incomePieChart.highlightValues(null)

        // loading chart
        binding.incomePieChart.invalidate()
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