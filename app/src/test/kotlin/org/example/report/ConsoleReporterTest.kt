package org.example.report

import org.example.error.ErrorKind
import org.example.error.RowError
import org.example.processing.ProcessingResult
import kotlin.test.Test
import kotlin.test.assertEquals

class ConsoleReporterTest {
    @Test
    fun `renders statistics and errors`() {
        val result = ProcessingResult(
            totalRows = 3,
            products = emptyList(),
            errors = listOf(
                RowError(1, ErrorKind.PARSING, "Не удалось распарсить цену 'abc'"),
                RowError(3, ErrorKind.VALIDATION, "Пустое название товара"),
            ),
        )

        val expected = """
            Статистика
            Обработано строк: 3
            Успешно загружено товаров: 0
            Ошибок парсинга: 1
            Ошибок валидации: 1

            Отчет об ошибках
            [ОШИБКА] Строка 1: Не удалось распарсить цену 'abc'
            [ОШИБКА] Строка 3: Пустое название товара
        """.trimIndent() + "\n"

        assertEquals(expected, ConsoleReporter.render(result))
    }
}
