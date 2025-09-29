package org.example

import java.time.format.DateTimeFormatter;
import java.util.Random
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class GestorPrestamos {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private val random = (100..1000).random()


    // Lista de libros disponibles en el sistema
    private val catalogo = mutableListOf<Libro>()
    public val librosArrendados = mutableListOf<Libro>()

    // Inicializa el catalogo de libros disponibles en el sistema
    fun inicializarCatalogo(){
        catalogo.addAll(
            listOf(
                LibroFisico("Estructuras de Datos", "Goodrich", 12990, 7, false),
                LibroFisico("Diccionario Enciclopédico", "Varios Autores", 15990, 0, true),
                LibroDigital("Programación en Kotlin", "JetBrains", 9990, 10, true),
                LibroDigital("Algoritmos Básicos", "Cormen", 11990, 10, false)
            )
        )
    }

    // Imprime el catalogo de libros disponibles en el sistema
    fun imprimirCatalogo() {
        catalogo.forEachIndexed { index, libro ->
            val tipo = if (libro is LibroFisico) "FÍSICO:" else "DIGITAL:"
            println("${index + 1}. $tipo - '${libro.titulo}'")
            println("   Autor: ${libro.autor}")
            println("   Precio: $${libro.precioBase}")
            println("   Días préstamo: ${libro.diasPrestamo}")

            when (libro) {
                is LibroFisico -> println("   Prestable: ${if (libro.esPrestable) "Sí" else "No"}")
                is LibroDigital -> println("   Descargable: ${if (libro.drm) "Sí" else "No"}")
            }
            println()
        }
    }

    // Simula un carrito, con los libros añadidos.
    fun añadirLibro(idLibro: Int){
        try {
            val libro = catalogo.get(idLibro)
            if (libro is LibroDigital || libro is LibroFisico && libro.esPrestable) {
                librosArrendados.add(libro)
                librosArrendados.forEachIndexed { index, libro ->
                    println("- Añadido: '${libro.titulo}', escrito por ${libro.autor}. \n  Precio Base: $${libro.precioBase}.")
                }
            } else {
                println("No se pudo arrendar el libro '${libro.titulo}', pues es sólo para referencias.")
            }
        }catch (e: IndexOutOfBoundsException){
            println("No existe un libro con la ID ingresada. Reintente.")
        }
    }

    fun calculaMulta(){}


    suspend fun aplicarDescuentos(categoriaUsuario: Int){

        //Suma de los precios de los libros arrendados.
        val totalSinDescuentos = librosArrendados.sumOf { it.precioBase }

        println("El total sin descuentos es de $${totalSinDescuentos}")
        println("Se está aplicando el descuento. Espere unos 3 segundos....")
        delay(3000)

        when(categoriaUsuario){

            1 -> {
                val precioEstudiante = (totalSinDescuentos * 0.9).roundToInt()
                presentarResultados(precioEstudiante, totalSinDescuentos)
            }

            2 -> {
                val precioDocente = (totalSinDescuentos * 0.85).roundToInt()
                presentarResultados(precioDocente, totalSinDescuentos)
            }

            3 -> {
                val precioExterno = totalSinDescuentos
                presentarResultados(precioExterno, totalSinDescuentos)
            }

            else -> {
                println("Valor inválido. Reintente")
            }

        }

        librosArrendados.clear()

    }

    // Muestra los libros arrendados en el sistema y una boleta con los precios y descuentos aplicados.
    fun presentarResultados(precioFinal : Int, totalSinDescuentos: Int){

        println("=".repeat(60))
        println("             Boleta de Ventas y Servicios\n" +
                "                Bibliotecas BookSmart\n" +
                "      Casa Matriz: NullPointerException 100, STGO\n" +
                "                 Boleta N° ${random}\n" +
                "\n" +
                "${librosArrendados.size} libros: ${totalSinDescuentos}\n" +
                "Descuentos                     : ${totalSinDescuentos - precioFinal}\n" +
                "Precio Final                   : ${precioFinal}\n" +
                "\n" +
                "     Si el producto no le ENCANTÓ entonces no vuelva...\n" +
                "                 Gracias por su compra.\n" +
                "                           :P")

        println("=".repeat(60))

    }


}