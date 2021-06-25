package com.bank.app.domain.usecases

import com.bank.app.data.entities.CardholderData
import com.bank.app.data.entities.State
import com.bank.app.data.entities.TransactionData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val getCardHolderInfo: GetCardHolderInfoUseCase,
    private val getCurrencyRates: GetCurrencyRatesUseCase,
    private val convertUserCurrency: ConvertUserCurrencyUseCase,
) {

    suspend operator fun invoke(): Map<CardholderData, List<TransactionData>>? {
        return coroutineScope {
            try {
                val cardHolderInfo = async { getCardHolderInfo() }
                val currencyRates = async { getCurrencyRates() }
                val cardHolders = when (val cardResult = cardHolderInfo.await()) {
                    is State.Success -> cardResult.data
                    is State.Error -> null
                }
                val currencies = when (val currencyResult = currencyRates.await()) {
                    is State.Success -> currencyResult.data
                    is State.Error -> null
                }
                if (cardHolders == null || currencies == null) return@coroutineScope null
                convertUserCurrency(cardHolders,currencies)
            } catch (e: Exception) {
                null
            }
        }
    }
}