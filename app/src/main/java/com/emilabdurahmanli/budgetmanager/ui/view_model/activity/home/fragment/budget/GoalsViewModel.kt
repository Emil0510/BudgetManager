package com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget

import android.icu.util.CurrencyAmount
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.budgetmanager.constants.Constant
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Goals
import com.emilabdurahmanli.budgetmanager.model.Operation
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalsViewModel : ViewModel() {

    private var goals = MutableLiveData<List<Goals>>()
    private var listGoals = mutableListOf<Goals>()
    private var addGoalOperation = MutableLiveData<Operation>()
    private lateinit var balance: Balance
    private var getBalanceOperation = MutableLiveData<Operation>()
    private var payOperation = MutableLiveData<Operation>()
    private var deleteOperation = MutableLiveData<Operation>()


    fun getGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Goals")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            listGoals.clear()
                            snapshot.children.forEach {
                                listGoals.add(it.getValue(Goals::class.java)!!)
                            }
                            goals.postValue(listGoals)
                        }else{
                            listGoals = mutableListOf()
                            goals.postValue(listGoals)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

    fun getBalance() {
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

    fun addGoals(currentTime: String, goalsTitle: String, soldAmount: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Goals")
                .child(currentTime)
                .setValue(Goals(goalsTitle, soldAmount, 0.0, payImage = null, currentTime))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        addGoalOperation.postValue(Operation(it.isSuccessful, "Goal added"))
                    } else {
                        addGoalOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Something went wrong"
                            )
                        )
                    }
                }
        }
    }

    fun payGoal(goalTime: String, payAmount: Double, currentTime: String, isCash: Boolean, goalsTitle: String, goalBeforePayAmount : Double) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Goals")
                .child(goalTime).child("payAmount").setValue(goalBeforePayAmount+payAmount).addOnCompleteListener {
                    val image = Constant.categoryImages.getValue("goal")
                    addToTransaction(isCash, image, "Goal", currentTime, payAmount, "", goalTime, goalsTitle)
                }
        }
    }


    private fun addToTransaction(
        isCash: Boolean,
        categoryImage: String,
        transactionCategory: String,
        transactionDate: String,
        transactionAmount: Double,
        moreTitleText: String,
        goalTime : String,
        goalsTitle: String
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
                            true,
                            goalTime,
                            goalsTitle
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            updateBalance(transactionAmount, isCash)
                        } else {
                            payOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Something went wrong"
                                )
                            )
                        }
                    }
            }
        } else {
            if (isCash) {
                payOperation.postValue(
                    Operation(
                        false,
                        "Your have not enough balance in cash"
                    )
                )
            } else {
                payOperation.postValue(
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
            balance.cash!! >= amount
        } else {
            balance.card!! >= amount
        }
    }

    private fun updateBalance(amount: Double, isCash: Boolean) {
        if (isCash) {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                .child("Balance").setValue(
                    Balance(
                        balance.cash!! - amount, balance.card
                    )
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        payOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Payed"
                            )
                        )
                    } else {
                        payOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Something went wrong"
                            )
                        )
                    }

                }
        } else {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString())
                .child("Balance")
                .setValue(Balance(balance?.cash, balance?.card!! - amount))
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        payOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Payed"
                            )
                        )
                    } else {
                        payOperation.postValue(
                            Operation(
                                it.isSuccessful,
                                "Something went wrong"
                            )
                        )
                    }
                }
        }

    }

    fun deleteGoal(goalTime : String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Goals")
                .child(goalTime).addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            snapshot.ref.removeValue().addOnSuccessListener {
                                deleteOperation.postValue(Operation(true, "Goal deleted"))
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }


    fun observeDeleteOperation() : LiveData<Operation>{
        return deleteOperation
    }

    fun observePayOperation():LiveData<Operation>{
        return payOperation
    }
    fun observeGetBalanceOperation(): LiveData<Operation>{
        return getBalanceOperation
    }

    fun observeGoals(): LiveData<List<Goals>> {
        return goals
    }

    fun observeAddGoalOperation(): LiveData<Operation> {
        return addGoalOperation
    }
}