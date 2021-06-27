package com.bank.app.domain.usecases

import android.util.Log
import com.bank.app.data.entities.CardholderData
import com.bank.app.data.entities.Currency
import com.bank.app.data.entities.TransactionData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val getCardHolderInfo: GetCardHolderInfoUseCase,
    private val getCurrencyRates: GetCurrencyRatesUseCase,
    private val convertUserCurrency: ConvertUserCurrencyUseCase,
) {

    suspend operator fun invoke(): Result<Pair<Map<CardholderData, List<TransactionData>>, Map<String, Currency>>> {
        return coroutineScope {
            val cardHolderInfo = async { getCardHolderInfo() }
            val currencyRates = async { getCurrencyRates() }

            val cardResult = cardHolderInfo.await()
            val cardHolders = cardResult.fold(
                onSuccess = {
                    it
                }, onFailure = {
                    return@coroutineScope Result.failure(it)
                })

            val currencyResult = currencyRates.await()
            val currencies = currencyResult.fold(
                onSuccess = {
                    it
                },
                onFailure = {
                    return@coroutineScope Result.failure(it)
                }
            )

            Result.success(convertUserCurrency(cardHolders, currencies) to currencies)
        }
    }
}