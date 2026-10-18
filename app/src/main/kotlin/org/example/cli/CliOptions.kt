package org.example.cli

data class CliOptions(
    val format: String?,
    val input: String,
    val output: String?,
) {
    companion object {
        const val DEFAULT_INPUT = "input.csv"

        fun parse(args: Array<String>): CliOptions {
            val values = args.associate { argument ->
                require(argument.startsWith("--") && '=' in argument) {
                    "Некорректный аргумент '$argument', ожидается --ключ=значение"
                }
                argument.removePrefix("--").substringBefore('=') to argument.substringAfter('=')
            }
            val unknown = values.keys - setOf("format", "input", "output")
            require(unknown.isEmpty()) { "Неизвестные аргументы: ${unknown.joinToString()}" }
            return CliOptions(
                format = values["format"],
                input = values["input"] ?: DEFAULT_INPUT,
                output = values["output"],
            )
        }
    }
}
