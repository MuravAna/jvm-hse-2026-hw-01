package org.example.parsing

import java.nio.file.Files
import java.nio.file.Path

data class CsvRow(val lineNumber: Int, val content: String)

object CsvReader {
    fun read(path: Path): List<CsvRow> = parseLines(Files.readAllLines(path, Charsets.UTF_8))

    fun parseLines(lines: List<String>): List<CsvRow> =
        lines.drop(1)
            .mapIndexed { index, line -> CsvRow(index + 1, line.removeSuffix("\r")) }
            .filter { it.content.isNotBlank() }
}
