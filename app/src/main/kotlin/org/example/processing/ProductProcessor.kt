package org.example.processing

import org.example.error.ErrorKind
import org.example.error.ParseException
import org.example.error.RowError
import org.example.error.RowResult
import org.example.parsing.CsvLineParser
import org.example.parsing.CsvRow
import org.example.parsing.ProductRowParser
import org.example.validation.ProductValidator

class ProductProcessor {
    fun process(rows: List<CsvRow>): ProcessingResult {
        val seenIds = mutableSetOf<Int>()
        val results = rows.map { row ->
            val result = processRow(row)
            if (result is RowResult.Success && !seenIds.add(result.product.id)) {
                RowResult.Failure(
                    RowError(row.lineNumber, ErrorKind.VALIDATION, "Повторяющийся идентификатор товара ${result.product.id}")
                )
            } else {
                result
            }
        }
        return ProcessingResult(
            totalRows = rows.size,
            products = results.filterIsInstance<RowResult.Success>().map { it.product },
            errors = results.filterIsInstance<RowResult.Failure>().map { it.error },
        )
    }

    fun processRow(row: CsvRow): RowResult {
        val product = try {
            ProductRowParser.parse(CsvLineParser.parse(row.content))
        } catch (e: ParseException) {
            return RowResult.Failure(RowError(row.lineNumber, ErrorKind.PARSING, e.message.orEmpty()))
        }
        val validationError = ProductValidator.validate(product) ?: return RowResult.Success(product)
        return RowResult.Failure(RowError(row.lineNumber, ErrorKind.VALIDATION, validationError))
    }
}
