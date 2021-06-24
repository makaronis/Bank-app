package com.bank.app.data.repository

import com.bank.app.data.api.CurrencyService
import com.bank.app.data.entities.Currency
import com.bank.app.domain.repository.CurrencyRatesRepo
import javax.inject.Inject

class CurrencyRatesRepoImpl @Inject constructor(
    private val apiService: CurrencyService
) : CurrencyRatesRepo {

    override suspend fun getCurrencies(): Map<String, Currency> {
        val response = apiService.getCurrency()
        return response.currencies
    }
}