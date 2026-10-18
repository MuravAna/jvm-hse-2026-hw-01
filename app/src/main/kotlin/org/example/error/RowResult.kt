package org.example.error

import org.example.model.Product

sealed class RowResult {
    data class Success(val product: Product) : RowResult()
    data class Failure(val error: RowError) : RowResult()
}
