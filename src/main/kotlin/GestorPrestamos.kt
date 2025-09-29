package org.example

import java.time.format.DateTimeFormatter
import kotlinx.coroutines.*
import kotlin.math.roundToInt
import java.time.LocalDate
import java.time.temporal.ChronoUnit



class GestorPrestamos {

    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val random = (1000..4000).random()


    // Lista de libros disponibles en el sistema
    private val catalogo = mutableListOf<Libro>()
    val librosArrendados = mutableListOf<Prestamo>()

    /*  -----------------------------------------------------------
        Inicializa el catalogo de libros disponibles en el sistema.
        -----------------------------------------------------------
    */
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

    /*  -------------------------------------------------------
        Imprime el catálogo de libros disponibles en el sistema.
        -------------------------------------------------------
    */
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



    /*  ----------------------------------------------------------------------------------------------
        Simula una adicion a una base de datos con los libros añadidos. Además maneja excepciones para
        cuando los libros no son prestables, o para cuando la ID ingresada del libro es inválida.
        ----------------------------------------------------------------------------------------------
    */
    fun anadirLibro(idLibro: Int){
        try {
            val libro = catalogo.get(idLibro)
            val prestable = when (libro) {
                is LibroDigital -> true
                is LibroFisico -> libro.esPrestable
                else -> false
            }
            if (!prestable) {
                println("No se pudo arrendar el libro '${libro.titulo}', pues es sólo para referencias.")
                return
            }
            // Evitar préstamo activo duplicado del mismo libro físico
            if (libro is LibroFisico && librosArrendados.any { it.libro == libro && it.estado is EstadoPrestamo.Activo }) {
                println("Ya existe un préstamo activo para '${libro.titulo}'.")
                return
            }
            val hoy = LocalDate.now()
            val fechaFin = hoy.plusDays(libro.diasPrestamo.toLong())
            val prestamo = Prestamo(
                libro = libro,
                fechaInicio = hoy,
                estado = EstadoPrestamo.Activo(fechaFin)
            )
            librosArrendados.add(prestamo)
            println("- Añadido: '${libro.titulo}', escrito por ${libro.autor}.")
            println("  Precio Base: $${libro.precioBase}. Fecha fin: ${fechaFin.format(formatter)}")
        }catch (e: IndexOutOfBoundsException){
            println("No existe un libro con la ID ingresada. Reintente.")
        }

    }




    /*  --------------------------------------------------------------------------------------------------------
        Calcula la multa que se debe pagar si el libro no se devuelve en el plazo estipulado.
        Cada libro tiene un valor 'diasPrestamo' que indica los días que ha sido prestado. Se usa
        un valor Random que simula un retraso en la entrega del libro en días. Con base en ese valor se calcula
        la multa.
        --------------------------------------------------------------------------------------------------------
    */
    fun calculaMulta(idDevolucion : Int){
        val index = idDevolucion - 1
        if (index !in librosArrendados.indices) {
            println("No existe un préstamo con esa ID. Reintente.")
            return
        }
        val prestamo = librosArrendados[index]

        // Simulación: hoy puede estar adelantado en días a definir para emular atraso
        // en entrega del libro. Entonces si hoy es realmente 29 de septiembre, y el libro
        // se entrega el 3 de octubre, el sistema aleatoriamente puede adelantarse entre
        // 5 a 8 días, y de esa manera simular estar (por ejemplo) en el 4 de octubre, ocurriendo
        // el retraso de la entrega, y aplicando la multa correspondiente.

        val hoy = LocalDate.now().plusDays((5..8).random().toLong())
        actualizarEstado(prestamo, hoy)

        val multa = when (val estado = prestamo.estado) {
            is EstadoPrestamo.Vencido -> (estado.diasAtraso * 500).toInt()
            is EstadoPrestamo.Activo -> 0
            is EstadoPrestamo.Devuelto -> {
                println("El préstamo ya fue devuelto el ${estado.fechaDevolucion.format(formatter)}.")
                return
            }
        }

        // Transición a Devuelto
        prestamo.estado = EstadoPrestamo.Devuelto(hoy)

        librosArrendados.removeAt(index)

        println("-".repeat(40))
        println("Devolución registrada para: '${prestamo.libro.titulo}'")
        println("Fecha devolución: ${hoy.format(formatter)}")
        if (multa > 0) {
            val venc = (prestamo.estado as EstadoPrestamo.Devuelto)
            println("Multa por atraso: $$multa")
        } else {
            println("Sin multa (dentro del plazo).")
        }
        println("-".repeat(40))
    }


    /*  --------------------------------------------------------------------------------------------------
        Una función simple que muestra los libros que ya han sido arrendados por el usuario, incluyendo su
        estado (Activo, Vencido, Devuelto).
        Util al momento de Devolver un Libro.
        No recibe parámetros.
        --------------------------------------------------------------------------------------------------
    */
    fun mostrarArrendados(){
        if (librosArrendados.isEmpty()) {
            println("No tienes préstamos activos.")
            return
        }
        librosArrendados.forEachIndexed { i, p ->
            actualizarEstado(p, LocalDate.now())
            val estadoTxt = when (val e = p.estado) {
                is EstadoPrestamo.Activo -> "Activo (vence: ${e.fechaFin.format(formatter)})"
                is EstadoPrestamo.Vencido -> "Vencido (${e.diasAtraso} día(s) atraso)"
                is EstadoPrestamo.Devuelto -> "Devuelto (${e.fechaDevolucion.format(formatter)})"
            }
            println("${i + 1}. '${p.libro.titulo}' - $estadoTxt")
        }

    }



    /*  ----------------------------------------------------------------------------------------------------
        Función con delay que simula el proceso de pago del libro. Lee la lista de librosArrendados, calcula
        el precio final y muestra el resutado llamando a la funcion presentarResultados().
        El delay simula el tiempo que tarda la boleta en generarse.
        ----------------------------------------------------------------------------------------------------
    */
    suspend fun aplicarDescuentos(categoriaUsuario: Int){

        //Suma de los precios de los libros arrendados.
        val totalSinDescuentos : Int = librosArrendados.sumOf { it.libro.precioBase }

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
    }




    /*  -----------------------------------------------------------------------
        Muestra una boleta con los precios y descuentos aplicados. Esta funcion
        es llamada desde aplicarDescuentos().
        -----------------------------------------------------------------------
    */
    fun presentarResultados(precioFinal : Int, totalSinDescuentos: Int) {

        println("=".repeat(60))
        println(
            "             Boleta de Ventas y Servicios\n" +
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
                    "                           :P"
        )

        println("=".repeat(60))
    }


    /* ----------------------------------------------------------------
       Actualiza estado a Vencido si corresponde según la fecha de hoy
       ----------------------------------------------------------------
    */
    private fun actualizarEstado(prestamo: Prestamo, hoy: LocalDate){
        when (val e = prestamo.estado) {
            is EstadoPrestamo.Activo -> {
                if (hoy.isAfter(e.fechaFin)) {
                    val atraso = ChronoUnit.DAYS.between(e.fechaFin, hoy)
                    prestamo.estado = EstadoPrestamo.Vencido(e.fechaFin, atraso)
                }
            }
            else -> { /* no-op */ }
        }
    }


    /*
        ------------------------------
        Easter Egg: Juego del Ahorcado
        ------------------------------
    */
    fun easterEggAhorcado() {
        val palabras = listOf("KOTLIN", "PROGRAMACION", "EASTER", "EGG", "TERMINAL", "JUEGO")
        val palabra = palabras.random()
        val palabraOculta = CharArray(palabra.length) { '_' }
        var intentos = 6
        val letrasUsadas = mutableSetOf<Char>()

        println("EASTER EGG: Ahorcado!")
        println("Adivina la palabra: ${palabraOculta.joinToString(" ")}")

        while (intentos > 0 && '_' in palabraOculta) {
            println("\nIntentos restantes: $intentos ❤")
            println("Letras usadas: ${letrasUsadas.sorted().joinToString()}")
            print("Ingresa una letra: ")

            val input = readLine()?.uppercase()?.firstOrNull()

            when {
                input == null -> println("Booo... Ingresa una letra")
                input in letrasUsadas -> println("Attentiii!!! Ya usaste esa letra")
                input in palabra -> {
                    println("¡Correcto! :3")
                    palabra.forEachIndexed { index, char ->
                        if (char == input) palabraOculta[index] = char
                    }
                }
                else -> {
                    println("Diantres!!! Letra incorrecta")
                    intentos--
                }
            }

            if (input != null) letrasUsadas.add(input)
            println("\nPalabra: ${palabraOculta.joinToString(" ")}")
        }

        println("\n" + "=".repeat(30))
        when {
            '_' !in palabraOculta -> println("¡Ganaste! La palabra era: $palabra")
            else -> println("Game Over! La palabra era: $palabra :P")
        }
    }

}