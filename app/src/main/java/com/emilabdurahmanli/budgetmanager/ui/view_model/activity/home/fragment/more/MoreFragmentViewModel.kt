package com.emilabdurahmanli.budgetmanager.ui.view_model.activity.home.fragment.more

import android.util.Log
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Income
import com.emilabdurahmanli.budgetmanager.model.Operation
import com.emilabdurahmanli.budgetmanager.model.User
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MoreFragmentViewModel : ViewModel() {
    private var listIncome = mutableListOf<Income>()
    private var incomes = MutableLiveData<List<Income>>()
    private var deleteOperation = MutableLiveData<Operation>()
    private var getBalanceOperation = MutableLiveData<Operation>()
    private var balance: Balance? = null
    private var userInfo = MutableLiveData<User>()
    fun getIncomes() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Incomes")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                listIncome.add(it.getValue(Income::class.java)!!)
                            }
                            incomes.postValue(listIncome)
                        }
                    }


                    override fun onCancelled(error: DatabaseError) {
                    }

                })


        }
    }

    fun deleteIncome(amount: Double, isCash: Boolean, incomeDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Incomes")
                .child(incomeDate)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.ref.removeValue().addOnSuccessListener {
                                updateBalance(amount, isCash)
                            }
                        }
                    }


                    override fun onCancelled(databaseError: DatabaseError) {
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
                            balance = snapshot.getValue(Balance::class.java)
                            getBalanceOperation.postValue(
                                Operation(
                                    snapshot.exists(),
                                    "Enable delete button"
                                )
                            )
                        } else {
                            getBalanceOperation.postValue(
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

    private fun updateBalance(amount: Double, isCash: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isCash) {
                FirebaseNetwork.databaseReference.child("Users")
                    .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance")
                    .setValue(
                        Balance(
                            balance?.cash?.minus(amount), balance?.card
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            deleteOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Income deleted successfully"
                                )
                            )
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
                            balance?.cash, balance?.card?.minus(amount)
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            deleteOperation.postValue(
                                Operation(
                                    it.isSuccessful,
                                    "Income deleted successfully"
                                )
                            )
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

    fun getUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("UserInfo")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userInfo.postValue(snapshot.getValue(User::class.java))
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

    fun observeUserInfo() : LiveData<User>{
        return userInfo
    }

    fun observeBalanceOperation(): LiveData<Operation> {
        return getBalanceOperation
    }

    fun observeDeleteOperation(): LiveData<Operation> {
        return deleteOperation
    }

    fun observeIncomes(): LiveData<List<Income>> {
        return incomes
    }
}