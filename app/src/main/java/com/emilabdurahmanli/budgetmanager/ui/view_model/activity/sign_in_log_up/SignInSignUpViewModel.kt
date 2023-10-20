package com.emilabdurahmanli.budgetmanager.ui.view_model.activity.sign_in_log_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.budgetmanager.model.Balance
import com.emilabdurahmanli.budgetmanager.model.Operation
import com.emilabdurahmanli.budgetmanager.model.User
import com.emilabdurahmanli.budgetmanager.network.FirebaseNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInSignUpViewModel : ViewModel() {
    private var logInOperationMessage = MutableLiveData<Operation>()
    private var signUpOperationMessage = MutableLiveData<Operation>()
    private var setBalanceOperationMessage = MutableLiveData<Operation>()
    fun signIn(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        logInOperationMessage.postValue(
                            Operation(
                                task.isSuccessful,
                                "Signed in successfully"
                            )
                        )
                    } else {
                        logInOperationMessage.postValue(
                            Operation(
                                task.isSuccessful,
                                "Wrong email or password"
                            )
                        )
                    }
                }
        }
    }

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addUser(firstName, lastName)
                    }
                }
        }
    }

    private fun addUser(firstName: String, lastName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("UserInfo").setValue(
                    User(
                        FirebaseNetwork.auth.currentUser?.email.toString(),
                        firstName,
                        lastName
                    )
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signUpOperationMessage.postValue(
                            Operation(
                                it.isSuccessful,
                                "Signed up successfully"
                            )
                        )
                    } else {
                        signUpOperationMessage.postValue(
                            Operation(
                                it.isSuccessful,
                                "Something Went Wrong"
                            )
                        )
                        FirebaseNetwork.auth.signOut()
                    }
                }
        }
    }

    fun setBalance(cash: Double, card: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseNetwork.databaseReference.child("Users")
                .child(FirebaseNetwork.auth.currentUser?.uid.toString()).child("Balance").setValue(Balance(cash, card)).addOnCompleteListener {
                if (it.isSuccessful) {
                    setBalanceOperationMessage.postValue(
                        Operation(
                            it.isSuccessful,
                            "Balance updated successfully"
                        )
                    )
                } else {
                    setBalanceOperationMessage.postValue(
                        Operation(
                            it.isSuccessful,
                            "Something went wrong"
                        )
                    )
                }
            }
        }
    }


    fun observeBalanceOperationMessage(): LiveData<Operation> {
        return setBalanceOperationMessage
    }

    fun observeSignInMessage(): LiveData<Operation> {
        return logInOperationMessage
    }

    fun observeSignUpMessage(): LiveData<Operation> {
        return signUpOperationMessage
    }

}