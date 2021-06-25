package com.bank.app.domain.data

import com.bank.app.data.entities.Currency
import kotlinx.serialization.descriptors.PrimitiveKind

object CurrencyProcessor {

    private const val BASE_VALUE = "USD"

    fun recountCurrency(
        recountKey: String,
        nominal: Double,
        baseCurrencyKey: String = BASE_VALUE,
        currencies: Map<String, Currency>
    ): Double {
        val baseValue = currencies[baseCurrencyKey] ?: return 0.0
        val recountValue = currencies[recountKey] ?: return 0.0
        return recountValue.value * nominal / baseValue.value

    }


}