package org.example.model

enum class ProductType(val code: String, val allowedKeys: Set<String>) {
    OIL("oil", setOf("volume")),
    TIRE("tire", setOf("diameter", "season")),
    ACCESSORY("accessory", setOf("material", "color"));

    companion object {
        fun fromCode(code: String): ProductType? = entries.firstOrNull { it.code == code }
    }
}
