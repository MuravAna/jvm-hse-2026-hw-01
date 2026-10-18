package org.example.validation

import org.example.model.Accessory
import org.example.model.Oil
import org.example.model.Product
import org.example.model.Tire

object ProductValidator {
    val ALLOWED_COLORS = setOf("черный", "белый", "серебристый")

    fun validate(product: Product): String? = when {
        product.name.isBlank() -> "Пустое название товара"
        product.priceRub < 0 -> "Цена не может быть отрицательной"
        product.priceRub == 0.0 -> "Цена должна быть положительной"
        product.weightKg < 0 -> "Вес не может быть отрицательным"
        product.weightKg == 0.0 -> "Вес должен быть положительным"
        else -> validateAttributes(product)
    }

    private fun validateAttributes(product: Product): String? = when (product) {
        is Oil -> product.volume?.takeIf { it <= 0 }?.let { "Объем должен быть положительным" }
        is Tire -> product.diameter?.takeIf { it <= 0 }?.let { "Диаметр должен быть положительным" }
        is Accessory -> product.color
            ?.takeIf { it.lowercase() !in ALLOWED_COLORS }
            ?.let { "Недопустимое значение 'color=$it'" }
    }
}
