package com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.budget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Goals
import com.emilabdurahmanli.budgetmanager.model.Operation
import com.emilabdurahmanli.budgetmanager.model.Transaction
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.IllegalFormatCodePointException

class BudgetViewModel : ViewModel() {
    private var balance = MutableLiveData<Balance>()
    private var goal : Goals? = null

    private var cashTransactions = MutableLiveData<List<Transaction>>()
    private var cardTransaction = MutableLiveData<List<Transaction>>()
    private var cashTransactionList = mutableListOf<Transaction>()
    private var cardTransactionList = mutableListOf<Transaction>()
    private var balanceOperation = MutableLiveData<Operation>()
    private var deleteOperation = MutableLiveData<Operation>()

    private var topTransaction = MutableLiveData<List<Transaction>>()
    private var listTransaction = mutableListOf<Transaction>()

    private var listOfAllTransaction = MutableLiveData<List<Transaction>>()
    fun getBalance(isGoal: Boolean, goalTime: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance")
                .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            balance.postValue(snapshot.getValue(Balance::class.java))
                            if(isGoal){
                                getGoal(goalTime)
                            }else{
                                balanceOperation.postValue(
                                    Operation(
                                        snapshot.exists(),
                                        "Enable button"
                                    )
                                )
                            }
                        } else {
                            balanceOperation.postValue(
                                Operation(
                                    snapshot.exists(),
                                    "Something went wrong"
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }


                })
        }
    }

    fun getTransactions() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Transactions")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            cashTransactionList.clear()
                            cardTransactionList.clear()
                            snapshot.children.forEach {
                                if (it.getValue(Transaction::class.java)?.isCash!!) {
                                    cashTransactionList.add(it.getValue(Transaction::class.java)!!)
                                } else {
                                    cardTransactionList.add(it.getValue(Transaction::class.java)!!)
                                }
                            }
                            cashTransactions.postValue(cashTransactionList)
                            cardTransaction.postValue(cardTransactionList)
                        }else{
                            cashTransactionList = mutableListOf()
                            cardTransactionList = mutableListOf()
                            cashTransactions.postValue(cashTransactionList)
                            cardTransaction.postValue(cardTransactionList)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }

                })


        }
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
        topTransaction.postValue(newList)
    }

    private fun updateBalance(amount: Double, isCash: Boolean, isGoal: Boolean, goalTime: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isCash) {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance")
                    .setValue(
                        Balance(
                            balance.value?.cash?.plus(amount), balance.value?.card
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if(isGoal){
                                updateGoal(amount,goalTime )
                            }else{
                                deleteOperation.postValue(
                                    Operation(
                                        it.isSuccessful,
                                        "Transaction deleted successfully"
                                    )
                                )
                            }

                        } else {
                            deleteOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Something went wrong"
                                )
                            )
                        }
                    }
            } else {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance")
                    .setValue(
                        Balance(
                            balance.value?.cash, balance.value?.card?.plus(amount)
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if(isGoal){
                                updateGoal(amount, goalTime)
                            }else{
                                deleteOperation.postValue(
                                    Operation(
                                        it.isSuccessful,
                                        "Transaction deleted successfully"
                                    )
                                )
                            }

                        } else {
                            deleteOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Something went wrong"
                                )
                            )
                        }
                    }
            }

        }
    }

    private fun getGoal(goalTime: String){
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users").child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Goals").child(goalTime).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        goal = snapshot.getValue(Goals::class.java)
                        balanceOperation.postValue(
                            Operation(
                                snapshot.exists(),
                                "Enable button"
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    private fun updateGoal(amount: Double, goalTime: String){
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users").child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Goals").child(goalTime).child("payAmount").setValue(
                goal?.payAmount?.minus(amount)
            ).addOnCompleteListener {
                if(it.isSuccessful){
                    deleteOperation.postValue(
                        Operation(
                            it.isSuccessful,
                            "Transaction deleted successfully"
                        )
                    )
                }else{
                    deleteOperation.postValue(
                        Operation(
                            it.isSuccessful,
                            "Something went wrong"
                        )
                    )
                }
            }
        }
    }

    fun delete(amount: Double, isCash: Boolean, transactionDate: String, isGoal : Boolean, goalTime : String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Transactions")
                .child(transactionDate).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.removeValue().addOnSuccessListener {
                        updateBalance(amount, isCash, isGoal, goalTime)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }


    fun observeTopTransaction() : LiveData<List<Transaction>>{
        return topTransaction
    }

    fun observeDeleteOperation(): LiveData<Operation> {
        return deleteOperation
    }

    fun observeBalanceOperation(): LiveData<Operation> {
        return balanceOperation
    }

    fun observeCashTransactions(): LiveData<List<Transaction>> {
        return cashTransactions
    }

    fun observeCardTransactions(): LiveData<List<Transaction>> {
        return cardTransaction
    }

    fun observeBalance(): LiveData<Balance> {
        return balance
    }

    fun observeListOfAllTransactions() : LiveData<List<Transaction>>{
        return listOfAllTransaction
    }
}