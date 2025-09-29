package org.example

fun main() {

    //Se inicializa la clase "GestorPrestamos"
    val gestor = GestorPrestamos()

    //Se inicializa el catalogo de libros disponibles en el sistema
    gestor.inicializarCatalogo()


    //Damos inicio al sistema
    println("*".repeat(70))
    println("Sistema de Gestión de Bibliotecas BookSmart.")
    println("*".repeat(70))


    var salir = false
    while (!salir) {
        println(
            "Seleccione una opcion numérica: \n" +
                    "1.- Mostrar libros disponibles.\n" +
                    "2.- Devolver un libro.\n" +
                    "3.- Salir del sistema\n"
        )

        val opcion = readLine()?.toIntOrNull() ?: 0

        when (opcion) {

            1 -> {
                println("-".repeat(20))
                println("Mostrar libros disponibles.")
                println("-".repeat(20))

                gestor.imprimirCatalogo()

                println(
                    "¿Desea arrendar algun libro?\n" +
                            "1.- Si\n" +
                            "2.- No\n"
                )
                val respuesta = readLine()?.toIntOrNull() ?: 0


                while (true) {
                    when (respuesta) {
                        1 -> {
                            println("Seleccione el libro a arrendar (ingrese valores numéricos):\n")
                            val selLibro = readLine()?.toIntOrNull() ?: 0

                            gestor.arrendarLibro(selLibro - 1)
                            println("Se ha arrendado el libro.\n")
                            break
                        }

                        2 -> {
                            println("-".repeat(20))
                            println("Volviendo al menú principal...")
                            println("-".repeat(20))
                            break
                        }

                        else -> {
                            println("-".repeat(20))
                            println("Ingrese una opción válida. Volviendo al menú principal...")
                            println("-".repeat(20))
                            break
                        }
                    }
                }
            }

            2 -> {
                println("-".repeat(20))
                println("Devolver un libro.")
                println("-".repeat(20))

            }

            3 -> {
                println("-".repeat(20))
                println("Saliendo del sistema.")
                println("-".repeat(20))
                salir = true
            }

            4 -> {
                gestor.librosArrendados.forEachIndexed { index, libro ->
                    println("Nombre:  ${libro.titulo}")
                }
            }

        }
    }
}