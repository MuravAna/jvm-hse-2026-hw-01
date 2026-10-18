package org.example.processing

import org.example.error.ErrorKind
import org.example.error.RowError
import org.example.model.Accessory
import org.example.model.Oil
import org.example.model.Season
import org.example.model.Tire
import org.example.model.VehicleType
import org.example.parsing.CsvReader
import org.example.parsing.CsvRow
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductProcessorTest {
    private val lines = listOf(
        "id,name,type,price,weight,vehicle,extra",
        "1,\"Mobil 1, Synthetic\",oil,\$12.5,10lb,PASSENGER,viscosity=5W-30;volume=4",
        "2,Castrol Magnatec,oil,300.5руб,1.2kg,PASSENGER,viscosity=10W-40;volume=1",
        "3,\"Michelin Primacy\",tire,15\$,0.3kg,PASSENGER,diameter=16;season=SUMMER",
        "4,Nokian Hakkapeliitta,tire,25руб,2lb,TRUCK,diameter=18;season=WINTER",
        "5,Щетки стеклоочистителя,accessory,100руб,0.1kg,PASSENGER,material=резина;color=черный",
        "6,\"Bosch, Air Filter\",accessory,\$7.5,0.05kg,MOTORCYCLE,material=бумага;color=белый",
        "7,Масло,oil,abc,0.5kg,PASSENGER,viscosity=5W-30",
        "8,,tire,10\$,0.2kg,PASSENGER,diameter=16",
        "9,Присадка,accessory,50руб,0.1kg,PASSENGER,material=пластик;color=red",
        "10,Шина,tire,-5\$,0.3kg,PASSENGER,diameter=17",
        "11,Масло,oil,100руб,0.5kg,PASSENGER,viscosity=5W-30;volume=abc",
        "12,Аккумулятор,battery,200руб,0.4kg,PASSENGER,material=свинец",
    )

    private val processor = ProductProcessor()

    @Test
    fun `processes example from task`() {
        val result = processor.process(CsvReader.parseLines(lines))

        assertEquals(12, result.totalRows)
        assertEquals(6, result.products.size)
        assertEquals(3, result.parsingErrors)
        assertEquals(3, result.validationErrors)
        assertEquals(
            listOf(
                RowError(7, ErrorKind.PARSING, "Не удалось распарсить цену 'abc'"),
                RowError(8, ErrorKind.VALIDATION, "Пустое название товара"),
                RowError(9, ErrorKind.VALIDATION, "Недопустимое значение 'color=red'"),
                RowError(10, ErrorKind.VALIDATION, "Цена не может быть отрицательной"),
                RowError(11, ErrorKind.PARSING, "Объем 'abc' не является числом"),
                RowError(12, ErrorKind.PARSING, "Неизвестный тип товара 'battery'"),
            ),
            result.errors,
        )
    }

    @Test
    fun `normalizes loaded products`() {
        val products = processor.process(CsvReader.parseLines(lines)).products

        assertEquals(
            listOf(
                Oil(1, "Mobil 1, Synthetic", 1125.0, 4.54, VehicleType.PASSENGER, volume = 4.0),
                Oil(2, "Castrol Magnatec", 300.5, 1.2, VehicleType.PASSENGER, volume = 1.0),
                Tire(3, "Michelin Primacy", 1350.0, 0.3, VehicleType.PASSENGER, diameter = 16.0, season = Season.SUMMER),
                Tire(4, "Nokian Hakkapeliitta", 25.0, 0.91, VehicleType.TRUCK, diameter = 18.0, season = Season.WINTER),
                Accessory(5, "Щетки стеклоочистителя", 100.0, 0.1, VehicleType.PASSENGER, material = "резина", color = "черный"),
                Accessory(6, "Bosch, Air Filter", 675.0, 0.05, VehicleType.MOTORCYCLE, material = "бумага", color = "белый"),
            ),
            products,
        )
    }

    @Test
    fun `reports structural parsing errors`() {
        val rows = listOf(
            CsvRow(1, "1,Масло,oil,10руб,1kg"),
            CsvRow(2, "x,Масло,oil,10руб,1kg,PASSENGER,"),
            CsvRow(3, "3,Масло,oil,10руб,1kg,BUS,"),
            CsvRow(4, "4,Шина,tire,10руб,1kg,TRUCK,season=SPRING"),
            CsvRow(5, "5,\"Масло,oil,10руб,1kg,TRUCK,"),
        )

        val result = processor.process(rows)

        assertEquals(5, result.parsingErrors)
        assertEquals(
            listOf(
                "Неверное количество полей: 5, ожидалось 7",
                "Некорректный идентификатор 'x'",
                "Неизвестный тип автомобиля 'BUS'",
                "Неизвестный сезон 'SPRING'",
                "Незакрытая кавычка в строке",
            ),
            result.errors.map { it.message },
        )
    }

    @Test
    fun `accepts row without extra column`() {
        val result = processor.process(listOf(CsvRow(1, "1,Масло,oil,10руб,1kg,PASSENGER")))

        assertEquals(listOf(Oil(1, "Масло", 10.0, 1.0, VehicleType.PASSENGER, volume = null)), result.products)
    }

    @Test
    fun `rejects duplicate id`() {
        val result = processor.process(
            listOf(
                CsvRow(1, "1,Масло,oil,10руб,1kg,PASSENGER,"),
                CsvRow(2, "1,Другое масло,oil,10руб,1kg,PASSENGER,"),
            )
        )

        assertEquals(1, result.products.size)
        assertEquals(listOf(RowError(2, ErrorKind.VALIDATION, "Повторяющийся идентификатор товара 1")), result.errors)
    }
}
