package org.example

import java.time.LocalDate

sealed class EstadoPrestamo {
    data class Activo(val fechaFin: LocalDate) : EstadoPrestamo()
    data class Vencido(val fechaFin: LocalDate, val diasAtraso: Long) : EstadoPrestamo()
    data class Devuelto(val fechaDevolucion: LocalDate) : EstadoPrestamo()
}

data class Prestamo(
    val libro: Libro,
    val fechaInicio: LocalDate,
    var estado: EstadoPrestamo
)