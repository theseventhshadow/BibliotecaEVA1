package org.example

import kotlin.coroutines.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


class GestorPrestamos {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private val catalogo = mutableListOf<Libro>()

    fun inicializarCatalogo(){
        catalogo.addAll(
            listOf(
                LibroFisico("Estructuras de Datos", "Goodrich", 12990, 7, false),
                LibroFisico("Diccionaeio Enciclopédico", "Varios", 15990, 0, true),
                LibroDigital("Programación en Kotlin", "JetBrains", 9990, 10, true),
                LibroDigital("Algoritmos Básicos", "Cormen", 11990, 10, false)
            )
        )
    }

    fun imprimirCatalogo() {
        catalogo.forEachIndexed { index, libro ->
            val tipo = if (libro is LibroFisico) "FÍSICO:" else "DIGITAL:"
            println("${index + 1}. $tipo - '${libro.titulo}'")
            println("   Autor: ${libro.autor}")
            println("   Precio: $${libro.precioBase}")
            println("   Días préstamo: ${libro.diasPrestamo}")

            when (libro) {
                is LibroFisico -> println("   DRM: ${if (libro.esPrestable) "Sí" else "No"}")
                is LibroDigital -> println("   Descargable: ${if (libro.drm) "Sí" else "No"}")
            }
            println()
        }
    }

    fun calculaMulta(){}

    fun aplicarDescuentos(){
        print("Inicializando o aplicar descuentos")
    }

    fun presentarResultados(){}


}