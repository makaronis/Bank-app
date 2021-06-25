package com.bank.app.domain.usecases

import com.bank.app.data.db.AppSharedPref
import com.bank.app.data.entities.*
import com.bank.app.domain.data.CurrencyProcessor
import javax.inject.Inject

class ConvertUserCurrencyUseCase @Inject constructor(
    private val sharedPref: AppSharedPref,
    private val currencyProcessor: CurrencyProcessor,
) {
    operator fun invoke(
        cardholdersInfo: List<CardholderInfo>,
        currencies: Map<String, Currency>,
    ): Map<CardholderData, List<TransactionData>> {
        val currencyKey = sharedPref.getLastCurrencyCode()
        val map = mutableMapOf<CardholderData, List<TransactionData>>()
        cardholdersInfo.forEach { cardholderInfo ->
            val cardHolderData = CardholderData(
                cardNumber = cardholderInfo.cardNumber,
                type = cardholderInfo.type,
                cardholderName = cardholderInfo.cardholderName,
                valid = cardholderInfo.valid,
                balance = cardholderInfo.balance,
                convertedBalance = currencyProcessor.recountCurrency(
                    recountKey = currencyKey,
                    nominal = cardholderInfo.balance,
                    currencies = currencies,
                )
            )
            val transactionsData = cardholderInfo.transactionHistory.mapNotNull {
                val amount = it.amount.toDoubleOrNull() ?: return@mapNotNull null
                TransactionData(
                    title = it.title,
                    iconUrl = it.iconUrl,
                    date = it.date,
                    amount = amount,
                    convertedAmount = currencyProcessor.recountCurrency(
                        recountKey = currencyKey,
                        nominal = amount,
                        currencies = currencies,
                    ),
                    convertedCode = currencyKey,
                )

            }
            map[cardHolderData] = transactionsData
        }
        return map
    }
}