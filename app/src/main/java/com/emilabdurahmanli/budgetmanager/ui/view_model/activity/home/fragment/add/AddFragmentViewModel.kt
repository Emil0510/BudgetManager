package com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.add

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.model.Operation
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddFragmentViewModel : ViewModel() {

    private var addIncomeOperation = MutableLiveData<Operation>()
    private var addExpenseOperation = MutableLiveData<Operation>()
    private var getBalanceOperation = MutableLiveData<Operation>()
    private var balance: Balance? = null

    fun addIncome(amount: Double, isCash: Boolean, incomeTitle: String, incomeDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Incomes")
                .child(incomeDate)
                .setValue(Income(amount, isCash, incomeTitle, incomeDate)).addOnCompleteListener {
                    if (it.isSuccessful) {
                        updateBalance(isIncome = true, amount, isCash)
                    } else {
                        addIncomeOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Something went wrong"
                            )
                        )
                    }
                }
        }
    }

    fun addExpense(
        isCash: Boolean,
        categoryImage: String,
        transactionCategory: String,
        transactionDate: String,
        transactionAmount: Double,
        moreTitleText: String
    ) {

        if (isAfford(isCash, transactionAmount)) {
            CoroutineScope(Dispatchers.IO).launch {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                    .child("Transactions")
                    .child(transactionDate)
                    .setValue(
                        Transaction(
                            isCash,
                            categoryImage,
                            transactionCategory,
                            transactionDate,
                            transactionAmount,
                            moreTitleText,
                            false,
                            "",
                            ""
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            updateBalance(isIncome = false, transactionAmount, isCash)
                            addExpenseOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Expense has added"
                                )
                            )
                        } else {
                            addExpenseOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Something went wrong"
                                )
                            )
                        }
                    }
            }
        } else {
            if(isCash){
                addExpenseOperation.postValue(
                    Operation(
                        false,
                        "Your have not enough balance in cash"
                    )
                )
            }else{
                addExpenseOperation.postValue(
                    Operation(
                        false,
                        "Your have not enough balance in card"
                    )
                )
            }

        }
    }

    private fun isAfford(isCash: Boolean, amount: Double): Boolean {
        return if (isCash) {
            balance?.cash!! >= amount
        } else {
            balance?.card!! >= amount
        }
    }

    fun getBalance() {
        // After operation balance have to be updated
        CoroutineScope(Dispatchers.IO).launch {

            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            balance = snapshot.getValue(Balance::class.java)!!
                            getBalanceOperation.postValue(
                                Operation(
                                    snapshot.exists(),
                                    "Enable confirm button"
                                )
                            )
                        } else {
                            getBalanceOperation.postValue(
                                Operation(
                                    snapshot.exists(),
                                    "Balance not found"
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

    private fun updateBalance(isIncome: Boolean, amount: Double, isCash: Boolean) {
        if (isIncome) {
            if (isCash) {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                    .child("Balance").setValue(
                        Balance(
                            balance?.cash!! + amount, balance?.card
                        )
                    ).addOnCompleteListener {
                        addIncomeOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Income has added"
                            )
                        )
                    }
            } else {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                    .child("Balance")
                    .setValue(Balance(balance?.cash, balance?.card!! + amount))
                    .addOnCompleteListener {
                        addIncomeOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Income has added"
                            )
                        )
                    }
            }

        } else {
            if (isCash) {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                    .child("Balance").setValue(
                        Balance(
                            balance?.cash!! - amount, balance?.card
                        )
                    ).addOnCompleteListener {
                        addIncomeOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Income has added"
                            )
                        )
                    }
            } else {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                    .child("Balance")
                    .setValue(Balance(balance?.cash, balance?.card!! - amount))
                    .addOnCompleteListener {
                        addIncomeOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Income has added"
                            )
                        )
                    }
            }
        }
    }

    fun observeIncomeOperation(): LiveData<Operation> {
        return addIncomeOperation
    }

    fun observeExpenseOperation(): LiveData<Operation> {
        return addExpenseOperation
    }

    fun observeBalance(): LiveData<Operation> {
        return getBalanceOperation
    }

}