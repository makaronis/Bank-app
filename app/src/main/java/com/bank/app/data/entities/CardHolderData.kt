package com.bank.app.data.entities

import kotlinx.serialization.SerialName
import java.sql.Struct

data class CardholderData(
    val cardNumber: String,
    val type: CardType,
    val cardholderName: String,
    val valid: String,
    val balance: Double,
    val convertedBalance: Double,
    val convertedCode: String,
)