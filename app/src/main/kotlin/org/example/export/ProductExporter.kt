package org.example.export

import org.example.model.Product

interface ProductExporter {
    val name: String
    val extension: String

    fun export(products: List<Product>): String
}
