package com.bank.app.presentation.utils

object Utils {

    fun getSymbolByCode(code: String): String {
        return when (code) {
            AppConfig.EUR_CURRENCY_CODE -> "€"
            AppConfig.RUB_CURRENCY_CODE -> "₽"
            AppConfig.GBP_CURRENCY_CODE -> "£"
            AppConfig.BASE_CURRENCY -> "$"
            else -> "₽"
        }
    }
}