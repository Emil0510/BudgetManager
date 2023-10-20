package com.emilabdurahmanli.budgetmanager.model

import java.io.Serializable

data class Goals(
    var goalTitle: String? = "",
    var totalAmount: Double? = 0.0,
    var payAmount: Double? = 0.0,
    var payImage: String? = "",
    var goalDate: String? = ""
) : Serializable
