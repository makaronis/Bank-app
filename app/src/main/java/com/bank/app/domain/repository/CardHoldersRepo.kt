package com.bank.app.domain.repository

import com.bank.app.data.entities.CardholderInfo

interface CardHoldersRepo {

    suspend fun getCardHoldersInfo(): List<CardholderInfo>
}