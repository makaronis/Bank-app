package com.bank.app.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardholderInfo(
    @SerialName("card_number")
    val cardNumber: String,
    val type: CardType,
    @SerialName("cardholder_name")
    val cardholderName: String,
    val valid: String,
    val balance: Double,
    @SerialName("transaction_history")
    val transactionHistory: List<Transaction>,
)

@Serializable
data class Transaction(
    val title: String,
    @SerialName("icon_url")
    val iconUrl: String,
    val date: String,
    val amount: String,
)

@Serializable
enum class CardType {
    @SerialName("mastercard")
    Mastercard,

    @SerialName("visa")
    Visa,

    @SerialName("unionpay")
    Unionpay
}