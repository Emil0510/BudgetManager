package com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.advice

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emilabdurahmanli.budgetmanager.R
import com.emilabdurahmanli.budgetmanager.databinding.FragmentAdvicesBinding
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.advice.adapter.AdviceOnClickListener
import com.emilabdurahmanli.budgetmanager.ui.activity.home.fragment.advice.adapter.AdvicesRecyclerViewAdapter


class AdvicesFragment : Fragment() {


    private lateinit var binding : FragmentAdvicesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdvicesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topFragment.backArrowIV.visibility = View.GONE
        binding.topFragment.fragmentNameText.setText("Advices")
        binding.advicesRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.advicesRV.adapter = AdvicesRecyclerViewAdapter(listOf(R.drawable.advices1, R.drawable.advices2, R.drawable.advices3, R.drawable.advices4), object : AdviceOnClickListener{
            override fun onClick() {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setMessage("You can only watch this on premium tariff")
                    .setTitle("Buy Premium \uD83D\uDC51")

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })

    }


}