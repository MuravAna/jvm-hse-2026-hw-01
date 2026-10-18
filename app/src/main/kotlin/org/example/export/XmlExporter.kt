package org.example.export

import org.example.model.Product

class XmlExporter : ProductExporter {
    override val name: String = "xml"
    override val extension: String = "xml"

    override fun export(products: List<Product>): String = buildString {
        append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        append("<products>\n")
        for (product in products) {
            append("  <product>\n")
            for ((key, value) in product.allFields()) {
                append("    <$key>${escape(value.toString())}</$key>\n")
            }
            append("  </product>\n")
        }
        append("</products>\n")
    }

    private fun escape(text: String): String = buildString {
        for (char in text) {
            when (char) {
                '&' -> append("&amp;")
                '<' -> append("&lt;")
                '>' -> append("&gt;")
                '"' -> append("&quot;")
                '\'' -> append("&apos;")
                else -> append(char)
            }
        }
    }
}
