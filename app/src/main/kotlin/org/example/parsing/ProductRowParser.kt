package org.example.parsing

import org.example.error.ParseException
import org.example.model.Accessory
import org.example.model.Oil
import org.example.model.Product
import org.example.model.ProductType
import org.example.model.Season
import org.example.model.Tire
import org.example.model.VehicleType
import org.example.normalization.Normalizer

object ProductRowParser {
    private const val COLUMNS_WITHOUT_EXTRA = 6
    private const val COLUMNS_WITH_EXTRA = 7

    fun parse(fields: List<String>): Product {
        if (fields.size != COLUMNS_WITHOUT_EXTRA && fields.size != COLUMNS_WITH_EXTRA) {
            throw ParseException("Неверное количество полей: ${fields.size}, ожидалось $COLUMNS_WITH_EXTRA")
        }
        val id = fields[0].trim().toIntOrNull()
            ?: throw ParseException("Некорректный идентификатор '${fields[0]}'")
        val name = fields[1].trim()
        val typeCode = fields[2].trim()
        val type = ProductType.fromCode(typeCode)
            ?: throw ParseException("Неизвестный тип товара '$typeCode'")
        val price = Normalizer.toRubles(ValueParsers.parsePrice(fields[3]))
        val weight = Normalizer.toKilograms(ValueParsers.parseWeight(fields[4]))
        val vehicleName = fields[5].trim()
        val vehicle = VehicleType.fromName(vehicleName)
            ?: throw ParseException("Неизвестный тип автомобиля '$vehicleName'")
        val extra = ValueParsers.parseExtra(fields.getOrElse(6) { "" }, type)

        return when (type) {
            ProductType.OIL -> Oil(
                id, name, price, weight, vehicle,
                volume = extra["volume"]?.let { parseNumberAttribute(it, "Объем") },
            )
            ProductType.TIRE -> Tire(
                id, name, price, weight, vehicle,
                diameter = extra["diameter"]?.let { parseNumberAttribute(it, "Диаметр") },
                season = extra["season"]?.let { parseSeason(it) },
            )
            ProductType.ACCESSORY -> Accessory(
                id, name, price, weight, vehicle,
                material = extra["material"],
                color = extra["color"],
            )
        }
    }

    private fun parseNumberAttribute(value: String, label: String): Double =
        ValueParsers.parseNumber(value) ?: throw ParseException("$label '$value' не является числом")

    private fun parseSeason(value: String): Season =
        Season.fromName(value) ?: throw ParseException("Неизвестный сезон '$value'")
}
