package com.bank.app.presentation.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.round(places: Int): Double {
    require(places >= 0)
    var bd = BigDecimal(this.toString())
    bd = bd.setScale(places, RoundingMode.HALF_UP)
    return bd.toDouble()
}