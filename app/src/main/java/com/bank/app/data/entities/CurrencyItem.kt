package com.bank.app.data.entities

data class CurrencyItem(
    val currencyCode: String,
    val currencySymbol: String,
    val isSelected: Boolean = false,
)