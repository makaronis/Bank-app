package com.bank.app.domain.usecases

import com.bank.app.data.entities.CardholderInfo
import com.bank.app.data.entities.UiState
import com.bank.app.domain.repository.CardHoldersRepo
import com.bank.bank_app.R
import javax.inject.Inject

class GetCardHolderInfoUseCase @Inject constructor(
    private val repo: CardHoldersRepo,
) {

    suspend operator fun invoke(): UiState<List<CardholderInfo>> {
        return try {
            val result = repo.getCardHoldersInfo()
            UiState.Success(result)
        } catch (e: Exception) {
            UiState.Error(msgId = R.string.error_no_internet)
        }
    }
}