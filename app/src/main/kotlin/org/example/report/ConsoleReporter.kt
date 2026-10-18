package org.example.report

import org.example.processing.ProcessingResult

object ConsoleReporter {
    fun render(result: ProcessingResult): String = buildString {
        append("Статистика\n")
        append("Обработано строк: ${result.totalRows}\n")
        append("Успешно загружено товаров: ${result.products.size}\n")
        append("Ошибок парсинга: ${result.parsingErrors}\n")
        append("Ошибок валидации: ${result.validationErrors}\n")
        if (result.errors.isNotEmpty()) {
            append("\nОтчет об ошибках\n")
            for (error in result.errors) {
                append("[ОШИБКА] Строка ${error.line}: ${error.message}\n")
            }
        }
    }
}
