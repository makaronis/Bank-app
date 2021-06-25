package com.bank.app.domain.usecases

import com.bank.app.data.entities.Currency
import com.bank.app.data.entities.UiState
import com.bank.app.domain.repository.CurrencyRatesRepo
import com.bank.bank_app.R
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val repo: CurrencyRatesRepo
) {

    suspend operator fun invoke(): UiState<Map<String, Currency>> {
        return try {
            val response = repo.getCurrencies()
            UiState.Success(response)
        } catch (e: Exception) {
            UiState.Error(msgId = R.string.error_no_internet)
        }
    }
}