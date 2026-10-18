package org.example.export

import org.example.model.Accessory
import org.example.model.Oil
import org.example.model.Product
import org.example.model.Tire

fun Product.commonFields(): List<Pair<String, Any>> = listOf(
    "id" to id,
    "type" to type.code,
    "name" to name,
    "price" to priceRub,
    "weight" to weightKg,
    "vehicle" to vehicle.name,
)

fun Product.attributes(): List<Pair<String, Any>> = when (this) {
    is Oil -> listOfNotNull(volume?.let { "volume" to it })
    is Tire -> listOfNotNull(diameter?.let { "diameter" to it }, season?.let { "season" to it.name })
    is Accessory -> listOfNotNull(material?.let { "material" to it }, color?.let { "color" to it })
}

fun Product.allFields(): List<Pair<String, Any>> = commonFields() + attributes()
