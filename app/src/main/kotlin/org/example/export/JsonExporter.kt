package org.example.export

import org.example.model.Product

class JsonExporter : ProductExporter {
    override val name: String = "json"
    override val extension: String = "json"

    override fun export(products: List<Product>): String {
        if (products.isEmpty()) {
            return "[]\n"
        }
        return products.joinToString(separator = ",\n", prefix = "[\n", postfix = "\n]\n") { product ->
            product.allFields().joinToString(separator = ",\n", prefix = "  {\n", postfix = "\n  }") { (key, value) ->
                "    \"${escape(key)}\": ${formatValue(value)}"
            }
        }
    }

    private fun formatValue(value: Any): String = when (value) {
        is Number -> value.toString()
        else -> "\"${escape(value.toString())}\""
    }

    private fun escape(text: String): String = buildString {
        for (char in text) {
            when {
                char == '"' -> append("\\\"")
                char == '\\' -> append("\\\\")
                char == '\n' -> append("\\n")
                char == '\r' -> append("\\r")
                char == '\t' -> append("\\t")
                char < ' ' -> append("\\u%04x".format(char.code))
                else -> append(char)
            }
        }
    }
}
