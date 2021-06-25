package com.bank.app.domain.usecases

import com.bank.app.data.entities.Currency
import com.bank.app.data.entities.State
import com.bank.app.domain.repository.CurrencyRatesRepo
import com.bank.bank_app.R
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val repo: CurrencyRatesRepo
) {

    suspend operator fun invoke(): State<Map<String, Currency>> {
        return try {
            val response = repo.getCurrencies()
            State.Success(response)
        } catch (e: Exception) {
            State.Error(msgId = R.string.error_no_internet)
        }
    }
}