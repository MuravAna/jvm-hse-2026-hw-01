package org.example.parsing

import org.example.error.ParseException

object CsvLineParser {
    private const val SEPARATOR = ','
    private const val QUOTE = '"'

    fun parse(line: String): List<String> {
        val fields = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var quotedFieldClosed = false
        var index = 0
        while (index < line.length) {
            val char = line[index]
            when {
                inQuotes && char == QUOTE && line.getOrNull(index + 1) == QUOTE -> {
                    current.append(QUOTE)
                    index++
                }
                inQuotes && char == QUOTE -> {
                    inQuotes = false
                    quotedFieldClosed = true
                }
                inQuotes -> current.append(char)
                char == SEPARATOR -> {
                    fields.add(current.toString())
                    current.clear()
                    quotedFieldClosed = false
                }
                quotedFieldClosed -> throw ParseException("Некорректное использование кавычек в строке")
                char == QUOTE && current.isEmpty() -> inQuotes = true
                else -> current.append(char)
            }
            index++
        }
        if (inQuotes) {
            throw ParseException("Незакрытая кавычка в строке")
        }
        fields.add(current.toString())
        return fields
    }
}
