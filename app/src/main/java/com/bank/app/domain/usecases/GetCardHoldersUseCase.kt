package com.bank.app.domain.usecases

import com.bank.app.data.entities.CardholderData
import com.bank.app.data.entities.TransactionData
import javax.inject.Inject

class GetCardHoldersUseCase @Inject constructor() {

    operator fun invoke(info: Map<String, Map.Entry<CardholderData, List<TransactionData>>>): List<CardholderData> {
        return info.values.map { it.key }
    }
}