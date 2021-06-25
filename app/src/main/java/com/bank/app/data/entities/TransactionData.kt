package com.bank.app.data.entities

import android.text.SpannableString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionData(
    val title: String,
    val iconUrl: String,
    val date: String,
    val amount: Double,
    val convertedAmount: Double,
    val convertedCode: String,
)