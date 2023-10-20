package com.emilabdurahmanli.budgetmanager.model

import java.io.Serializable

data class Income(var amount: Double? = 0.0, var isCash: Boolean? = false, var incomeTitle: String? = "", var incomeDate : String? = "") : Serializable
