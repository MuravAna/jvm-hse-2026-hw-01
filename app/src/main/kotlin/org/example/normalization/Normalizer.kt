package org.example.normalization

import org.example.model.Currency
import org.example.model.Price
import org.example.model.Weight
import org.example.model.WeightUnit
import kotlin.math.round

object Normalizer {
    const val USD_TO_RUB = 90.0
    const val LB_TO_KG = 0.454

    fun toRubles(price: Price): Double = when (price.currency) {
        Currency.RUB -> price.amount
        Currency.USD -> roundToHundredths(price.amount * USD_TO_RUB)
    }

    fun toKilograms(weight: Weight): Double = when (weight.unit) {
        WeightUnit.KG -> weight.amount
        WeightUnit.LB -> roundToHundredths(weight.amount * LB_TO_KG)
    }

    private fun roundToHundredths(value: Double): Double = round(value * 100) / 100
}
