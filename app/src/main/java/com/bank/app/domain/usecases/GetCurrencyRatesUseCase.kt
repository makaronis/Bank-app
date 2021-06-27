package com.bank.app.domain.usecases

import com.bank.app.data.entities.Currency
import com.bank.app.domain.repository.CurrencyRatesRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrencyRatesUseCase @Inject constructor(
    private val repo: CurrencyRatesRepo,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(): Result<Map<String, Currency>> {
        return try {
            val response = withContext(ioDispatcher) { repo.getCurrencies() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}