package org.example

import org.example.cli.CliOptions
import org.example.export.ExporterRegistry
import org.example.export.ProductExporter
import org.example.parsing.CsvReader
import org.example.processing.ProductProcessor
import org.example.report.ConsoleReporter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val options = try {
        CliOptions.parse(args)
    } catch (e: IllegalArgumentException) {
        fail(e.message.orEmpty())
    }
    val registry = ExporterRegistry.withDefaults()
    val exporter = options.format
        ?.let { registry.get(it) ?: fail("Неизвестный формат '$it'. Доступные: ${registry.available().joinToString()}") }
        ?: askFormat(registry)

    val inputPath = Path.of(options.input)
    if (!Files.isRegularFile(inputPath)) {
        fail("Файл '${options.input}' не найден")
    }

    val result = ProductProcessor().process(CsvReader.read(inputPath))
    print(ConsoleReporter.render(result))

    val outputPath = Path.of(options.output ?: "output.${exporter.extension}")
    Files.writeString(outputPath, exporter.export(result.products), Charsets.UTF_8)
    println("\nРезультат сохранен в файл: $outputPath")
}

private fun askFormat(registry: ExporterRegistry): ProductExporter {
    val formats = registry.available().joinToString()
    while (true) {
        print("Выберите формат сохранения ($formats): ")
        val answer = readlnOrNull() ?: fail("Формат сохранения не выбран")
        registry.get(answer)?.let { return it }
        println("Неизвестный формат '$answer'")
    }
}

private fun fail(message: String): Nothing {
    System.err.println(message)
    exitProcess(1)
}
