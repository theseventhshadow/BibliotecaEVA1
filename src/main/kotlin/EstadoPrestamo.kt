package org.example

sealed class EstadoPrestamo {
    object Pendiente : EstadoPrestamo()
    object EnPrestamo : EstadoPrestamo()
    object Devuelto : EstadoPrestamo()
}