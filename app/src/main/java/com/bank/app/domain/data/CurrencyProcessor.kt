package com.bank.app.domain.data

import com.bank.app.data.entities.Currency
import com.bank.app.presentation.utils.AppConfig

class CurrencyProcessor {

    fun recountCurrency(
        recountKey: String,
        nominal: Double,
        baseCurrencyKey: String = AppConfig.BASE_CURRENCY,
        currencies: Map<String, Currency>
    ): Double {
        val baseValue = currencies[baseCurrencyKey] ?: return 0.0
        val recountValue = if (recountKey == API_BASE_VALUE) {
            return calculateInBaseApiCurrency(nominal, baseValue)
        } else {
            currencies[recountKey] ?: return 0.0
        }
        val convertedInRub = baseValue.value * nominal
        return convertedInRub / recountValue.value
    }

    private fun calculateInBaseApiCurrency(nominal: Double, baseCurrency: Currency): Double {
        return nominal * baseCurrency.value
    }

    companion object {
        private const val API_BASE_VALUE = "RUB"
    }
}