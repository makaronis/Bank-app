package com.bank.app.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.bank.app.data.entities.CardType
import com.bank.bank_app.R

object ImageUtils {

    fun getImageByCardType(cardType: CardType, context: Context): Drawable? {
        val resId = when (cardType) {
            CardType.Mastercard -> R.drawable.ic_master_card
            CardType.Unionpay -> R.drawable.ic_union_pay
            CardType.Visa -> R.drawable.ic_visa
        }
        return ContextCompat.getDrawable(context, resId)
    }
}