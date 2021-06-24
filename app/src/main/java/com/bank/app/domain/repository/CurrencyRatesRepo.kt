package com.bank.app.domain.repository

import com.bank.app.data.entities.Currency

interface CurrencyRatesRepo {

    suspend fun getCurrencies(): Map<String, Currency>
}