package com.bank.app.domain.usecases

import com.bank.app.data.db.AppSharedPref
import com.bank.app.data.entities.*
import com.bank.app.domain.data.CurrencyProcessor
import javax.inject.Inject

class ChangeCurrencyUseCase @Inject constructor(
    private val convertUserCurrency: ConvertUserCurrencyUseCase,
    private val currencyProcessor: CurrencyProcessor,
    private val sharedPref: AppSharedPref,
) {

    operator fun invoke(
        newCurrencyCode: String,
        cardholdersInfoMap: Map<String, Map.Entry<CardholderData, List<TransactionData>>>,
        currencies: Map<String, Currency>,
    ): Map<CardholderData, List<TransactionData>> {
        sharedPref.setLastCurrencyCode(newCurrencyCode)
        val cardholdersInfo = cardholdersInfoMap.values
        val convertedMap = mutableMapOf<CardholderData, List<TransactionData>>()
        cardholdersInfo.forEach { entry ->
            val convertedCardholderInfo = entry.key.copy(
                convertedBalance = currencyProcessor.recountCurrency(
                    recountKey = newCurrencyCode,
                    nominal = entry.key.balance,
                    currencies = currencies,
                )
            )
            val convertedTransactions = entry.value.map {
                it.copy(
                    convertedAmount = currencyProcessor.recountCurrency(
                        recountKey = newCurrencyCode,
                        nominal = it.amount,
                        currencies = currencies,
                    )
                )
            }
            convertedMap[convertedCardholderInfo] = convertedTransactions
        }
        return convertedMap
    }
}