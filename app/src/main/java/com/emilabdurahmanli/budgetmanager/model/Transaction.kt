package com.emilabdurahmanli.budgetmanager.model

import java.io.Serializable

data class Transaction(
    var isCash: Boolean? = false,
    var categoryImage: String? = "",
    var transactionCategory: String? = "",
    var transactionDate: String? = "",
    var transactionAmount: Double? = 0.0,
    var moreTransactionTitle : String? = "",
    var isGoal : Boolean? = false,
    var goalDate : String? = "",
    var goalTitle : String? = ""
) : Serializable
