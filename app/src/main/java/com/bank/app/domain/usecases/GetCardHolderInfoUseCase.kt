package com.bank.app.domain.usecases

import com.bank.app.data.entities.CardholderInfo
import com.bank.app.domain.repository.CardHoldersRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCardHolderInfoUseCase @Inject constructor(
    private val repo: CardHoldersRepo,
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(): Result<List<CardholderInfo>> {
        return try {
            val result = withContext(ioDispatcher) { repo.getCardHoldersInfo() }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}