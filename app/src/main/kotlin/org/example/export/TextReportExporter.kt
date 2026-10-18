package org.example.export

import org.example.model.Product

class TextReportExporter : ProductExporter {
    override val name: String = "txt"
    override val extension: String = "txt"

    private val attributeLabels = mapOf(
        "volume" to "Объем, л",
        "diameter" to "Диаметр, дюймы",
        "season" to "Сезон",
        "material" to "Материал",
        "color" to "Цвет",
    )

    override fun export(products: List<Product>): String = buildString {
        append("Отчет по товарам\n")
        append("Всего товаров: ${products.size}\n")
        for (product in products) {
            append("\n")
            append("#${product.id} ${product.name}\n")
            append("  Тип: ${product.type.code}\n")
            append("  Цена, руб: ${product.priceRub}\n")
            append("  Вес, кг: ${product.weightKg}\n")
            append("  Транспорт: ${product.vehicle.name}\n")
            for ((key, value) in product.attributes()) {
                append("  ${attributeLabels[key] ?: key}: $value\n")
            }
        }
    }
}
