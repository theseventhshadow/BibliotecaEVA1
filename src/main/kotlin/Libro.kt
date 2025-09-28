package org.example

open class Libro(
    val titulo: String,
    val autor: String,
    val precioBase: Int,
    val diasPrestamo: Int
)

class LibroFisico(
    titulo: String,
    autor: String,
    precioBase: Int,
    diasPrestamo: Int,
    val esPrestable: Boolean
) : Libro(titulo, autor, precioBase, diasPrestamo){}

class LibroDigital(
    titulo: String,
    autor: String,
    precioBase: Int,
    diasPrestamo: Int,
    val drm: Boolean
) : Libro(titulo, autor, precioBase, diasPrestamo){}