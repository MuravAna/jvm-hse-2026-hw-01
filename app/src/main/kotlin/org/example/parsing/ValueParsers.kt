package org.example.parsing

import org.example.error.ParseException
import org.example.model.Currency
import org.example.model.Price
import org.example.model.ProductType
import org.example.model.Weight
import org.example.model.WeightUnit

object ValueParsers {
    private val NUMBER = Regex("-?\\d+(\\.\\d+)?")
    private const val DOLLAR = "$"
    private const val RUBLE = "руб"
    private const val KILOGRAM = "kg"
    private const val POUND = "lb"

    fun parseNumber(raw: String): Double? = raw.trim().takeIf { NUMBER.matches(it) }?.toDouble()

    fun parsePrice(raw: String): Price {
        val value = raw.trim()
        val (amount, currency) = when {
            value.startsWith(DOLLAR) -> value.removePrefix(DOLLAR) to Currency.USD
            value.endsWith(DOLLAR) -> value.removeSuffix(DOLLAR) to Currency.USD
            value.endsWith(RUBLE) -> value.removeSuffix(RUBLE) to Currency.RUB
            else -> throw ParseException("Не удалось распарсить цену '$raw'")
        }
        val number = parseNumber(amount) ?: throw ParseException("Не удалось распарсить цену '$raw'")
        return Price(number, currency)
    }

    fun parseWeight(raw: String): Weight {
        val value = raw.trim()
        val (amount, unit) = when {
            value.endsWith(KILOGRAM) -> value.removeSuffix(KILOGRAM) to WeightUnit.KG
            value.endsWith(POUND) -> value.removeSuffix(POUND) to WeightUnit.LB
            else -> throw ParseException("Не удалось распарсить вес '$raw'")
        }
        val number = parseNumber(amount) ?: throw ParseException("Не удалось распарсить вес '$raw'")
        return Weight(number, unit)
    }

    fun parseExtra(raw: String, type: ProductType): Map<String, String> =
        raw.split(';')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { part ->
                val separatorIndex = part.indexOf('=')
                if (separatorIndex <= 0) {
                    throw ParseException("Некорректная характеристика '$part', ожидается формат ключ=значение")
                }
                part.substring(0, separatorIndex).trim() to part.substring(separatorIndex + 1).trim()
            }
            .filter { (key, _) -> key in type.allowedKeys }
            .toMap()
}
