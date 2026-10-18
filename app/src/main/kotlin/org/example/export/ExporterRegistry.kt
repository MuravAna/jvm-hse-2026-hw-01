package org.example.export

class ExporterRegistry {
    private val exporters = linkedMapOf<String, ProductExporter>()

    fun register(exporter: ProductExporter) {
        val key = exporter.name.lowercase()
        require(key !in exporters) { "Формат '${exporter.name}' уже зарегистрирован" }
        exporters[key] = exporter
    }

    fun get(name: String): ProductExporter? = exporters[name.trim().lowercase()]

    fun available(): List<String> = exporters.keys.toList()

    companion object {
        fun withDefaults(): ExporterRegistry = ExporterRegistry().apply {
            register(JsonExporter())
            register(XmlExporter())
            register(TextReportExporter())
        }
    }
}
