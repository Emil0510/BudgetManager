package com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections


class DashboardFragmentViewModel : ViewModel() {

    private var balance = MutableLiveData<Balance>()
    private var income = MutableLiveData<List<Income>>()
    private var listIncome = mutableListOf<Income>()


    private var transaction = MutableLiveData<List<Transaction>>()
    private var listTransaction = mutableListOf<Transaction>()

    private var listOfAllTransaction = MutableLiveData<List<Transaction>>()


    fun getBalance() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            balance.postValue(snapshot.getValue(Balance::class.java))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }


                })
        }
    }

    fun getIncomes() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Incomes")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listIncome.clear()
                            snapshot.children.forEach {
                                listIncome.add(it.getValue(Income::class.java)!!)
                            }
                            selectIncomes()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

    private fun selectIncomes() {
        val cashList = mutableListOf<Income>()
        val cardList = mutableListOf<Income>()

        listIncome.forEach {
            if (it.isCash == true) {
                cashList.add(it)
            } else {
                cardList.add(it)
            }
        }
        var cashAmount: Double = 0.0
        cashList.forEach {
            cashAmount += it.amount!!
        }
        var cardAmount: Double = 0.0
        cardList.forEach {
            cardAmount += it.amount!!
        }

        val newList = listOf(Income(cashAmount, true, "", ""), Income(cardAmount, false, "", ""))
        income.postValue(newList)
    }

    fun getExpenses() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Transactions")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listTransaction.clear()
                            snapshot.children.forEach {
                                listTransaction.add(it.getValue(Transaction::class.java)!!)
                            }
                            selectExpense()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

    private fun selectExpense() {
        val transportTransaction = mutableListOf<Transaction>()
        val beautyTransaction = mutableListOf<Transaction>()
        val clothingTransaction = mutableListOf<Transaction>()
        val foodTransaction = mutableListOf<Transaction>()
        val donationTransaction = mutableListOf<Transaction>()
        val healthTransaction = mutableListOf<Transaction>()
        val homeTransaction = mutableListOf<Transaction>()
        val moreTransaction = mutableListOf<Transaction>()
        val giftTransaction = mutableListOf<Transaction>()
        val goalTransaction = mutableListOf<Transaction>()


        listTransaction.sortBy { it.transactionCategory }
        listTransaction.forEach {
            when (it.transactionCategory) {
                "Transport" -> {
                    transportTransaction.add(it)
                }

                "Beauty" -> {
                    beautyTransaction.add(it)
                }

                "Clothing" -> {
                    clothingTransaction.add(it)
                }

                "Food" -> {
                    foodTransaction.add(it)
                }

                "Donation" -> {
                    donationTransaction.add(it)
                }

                "Health" -> {
                    healthTransaction.add(it)
                }

                "Home" -> {
                    homeTransaction.add(it)
                }

                "More" -> {
                    moreTransaction.add(it)
                }

                "Gift" -> {
                    giftTransaction.add(it)
                }
                "Goal" ->{
                    goalTransaction.add(it)
                }

            }
        }

        val allTransactionList = listOf(
            transportTransaction,
            beautyTransaction,
            clothingTransaction,
            foodTransaction,
            donationTransaction,
            healthTransaction,
            homeTransaction,
            moreTransaction,
            giftTransaction,
            goalTransaction
        )


        val allTransaction = mutableListOf<Transaction>()
        val listOfAllTransaction = mutableListOf<Transaction>()
        for (i in 0..9) {
            var amount: Double = 0.0
            allTransactionList[i].forEach {
                amount += it.transactionAmount!!
            }
            if (allTransactionList[i].size != 0) {
                allTransaction.add(
                    Transaction(
                        false,
                        allTransactionList[i][0].categoryImage,
                        allTransactionList[i][0].transactionCategory,
                        allTransactionList[i][0].transactionDate,
                        amount,
                        ""
                    )
                )
                listOfAllTransaction.add(Transaction(
                    false,
                    allTransactionList[i][0].categoryImage,
                    allTransactionList[i][0].transactionCategory,
                    allTransactionList[i][0].transactionDate,
                    amount,
                    ""
                ))
            }else{
                listOfAllTransaction.add(Transaction(false, "", "", "", 0.0,"", false , "", "" ))
            }
        }

        this.listOfAllTransaction.postValue(listOfAllTransaction)

        allTransaction.sortByDescending { it.transactionAmount }
        val newList: List<Transaction>
        when (allTransaction.size) {
            0 -> {
                newList = listOf()
            }

            1 -> {
                newList = listOf(allTransaction[0])
            }

            else -> {
                newList = listOf(allTransaction[0], allTransaction[1])
            }
        }
        Log.d("Emilll", newList.toString())
        transaction.postValue(newList)
    }

    fun observeTwoExpense(): LiveData<List<Transaction>> {
        return transaction
    }


    fun observeIncomes(): LiveData<List<Income>> {
        return income
    }

    fun observeBalance(): LiveData<Balance> {
        return balance
    }

    fun observeListOfAllTransactions() : LiveData<List<Transaction>>{
        return listOfAllTransaction
    }
}