package org.example.processing

import org.example.error.ErrorKind
import org.example.error.RowError
import org.example.model.Product

data class ProcessingResult(
    val totalRows: Int,
    val products: List<Product>,
    val errors: List<RowError>,
) {
    val parsingErrors: Int get() = errors.count { it.kind == ErrorKind.PARSING }
    val validationErrors: Int get() = errors.count { it.kind == ErrorKind.VALIDATION }
}
