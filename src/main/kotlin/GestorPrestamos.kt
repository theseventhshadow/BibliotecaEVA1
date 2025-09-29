package org.example

import java.time.format.DateTimeFormatter;
import java.util.Random
import kotlinx.coroutines.*
import kotlin.math.roundToInt


class GestorPrestamos {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    val random = (1000..4000).random()


    // Lista de libros disponibles en el sistema
    private val catalogo = mutableListOf<Libro>()
    public val librosArrendados = mutableListOf<Libro>()

    // Inicializa el catalogo de libros disponibles en el sistema
    fun inicializarCatalogo(){
        catalogo.addAll(
            listOf(
                LibroFisico("Estructuras de Datos", "Goodrich", 12990, 0, false),
                LibroFisico("Diccionario Enciclopédico", "Varios Autores", 15990, 7, true),
                LibroDigital("Programación en Kotlin", "JetBrains", 9990, 5, true),
                LibroDigital("Algoritmos Básicos", "Cormen", 11990, 4, false)
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

    /*
        Simula un carrito, con los libros añadidos. Además maneja excepciones para
        cuando los libros no son prestables, o para cuando la ID ingresada del libro es inválida.
    */
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

    /*
        Calcula la multa que se debe pagar si el libro no se devuelve en el plazo estipulado.
        Cada libro tiene un valor 'diasPrestamo' que indica los dias que ha sido prestado. Se usa
        un valor Random que simula un retraso en la entrega del libro en dias. En base a ese valor se calcula
        la multa.
    */
    fun calculaMulta(){}


    /*
        Función con delay que simula el proceso de pago del libro. Lee la lista de librosArrendados, calcula
        el precio final y muestra el resutado llamando a la funcion presentarResultados().
        El delay simula el tiempo que tarda la boleta en generarse.
    */

    suspend fun aplicarDescuentos(categoriaUsuario: Int){

        //Suma de los precios de los libros arrendados.
        val totalSinDescuentos = librosArrendados.sumOf { it.precioBase }

        println("El total sin descuentos es de $${totalSinDescuentos}")

        if (categoriaUsuario == 1){
            println("Se ha aplicado el descuento de Estudiante (10%)\n" +
                    "Generando boleta...")
        }else if (categoriaUsuario == 2){
            println("Se ha aplicado el descuento de Docente (15%)\n" +
                    "Generando boleta...")
        }else if (categoriaUsuario == 3){
            println("Booo!!! Los externos no tienen descuento\n" +
                    "Generando boleta...")
        }

        // Aplicamos un tiempo de delay que simula un proceso en ejecucion...
        delay(4000)

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

    /*
     Muestra una boleta con los precios y descuentos aplicados. Esta funcion
     es llamada desde aplicarDescuentos().
    */
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