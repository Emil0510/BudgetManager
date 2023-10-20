package com.emilabdurahmanli.budgetmanager.network

import androidx.collection.arrayMapOf
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

object FirebaseNetwork {
    val databaseReference = FirebaseDatabase.getInstance().reference
    val auth = Firebase.auth
    val firebaseStorage = Firebase.storage



}