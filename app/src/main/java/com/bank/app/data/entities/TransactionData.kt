package com.bank.app.data.entities

import android.text.SpannableString
import kotlinx.serialization.SerialName

data class TransactionData(
    val title: String,
    val iconUrl: String,
    val date: String,
    val amount: String,
    val convertedAmount: SpannableString
)