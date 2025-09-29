package org.example

import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
coroutineContext

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
                    "¿Qué desea hacer?\n" +
                            "1.- Agregar libros a mi carrito\n" +
                            "2.- Ir al pago\n" +
                            "3.- Cancelar el proceso.\n"
                )
                val respuesta = readLine()?.toIntOrNull() ?: 0


                while (true) {
                    when (respuesta) {
                        1 -> {
                            println("Seleccione el libro a arrendar (ingrese valores numéricos):\n")
                            val selLibro = readLine()?.toIntOrNull() ?: 0

                            println("-".repeat(20))
                            println("Informacion sobre los libros:")
                            gestor.anadirLibro(selLibro - 1)
                            println("-".repeat(20))
                            break
                        }

                        2 -> {
                            println("-".repeat(20))
                            println("Ir al pago.")
                            println("-".repeat(20))

                            if (gestor.librosArrendados.size > 0){
                                println("Seleccione su Categoría de Usuario:\n" +
                                        "1.- Estudiante.\n" +
                                        "2.- Docente.\n" +
                                        "3.- Externo.\n")

                                val categoriaUsuario = readLine()?.toIntOrNull() ?: 0

                                gestor.aplicarDescuentos(categoriaUsuario)

                                break
                            }
                            else{
                                println("=".repeat(60))
                                print("No tienes productos añadidos en tu carrito. :P\n")
                                println("=".repeat(60))
                                break
                            }
                        }


                        3 -> {
                            println("-".repeat(20))
                            println("Volver atrás.")
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

                println("*** Tienes ${gestor.librosArrendados.size} libro(s) arrendados: ***\n")
                if (gestor.librosArrendados.size > 0){
                    gestor.mostrarArrendados()
                    println("\nIngrese el ID del libro a devolver:\n")
                    val idDevolucion = readLine()?.toIntOrNull() ?: 0

                    gestor.calculaMulta(idDevolucion)
                }
                else{
                    println("=".repeat(50))
                    print("No tienes productos para devolver. :P\n")
                    println("=".repeat(50))
                }



            }

            3 -> {
                println("-".repeat(20))
                println("Saliendo del sistema.")
                println("-".repeat(20))
                salir = true
            }

            4 -> {
                println("-".repeat(20))
                println("Easter Egg.")
                println("-".repeat(20))

                gestor.easterEggAhorcado()
            }

            else -> {
                println("-".repeat(20))
                println("La opcion ingresada no existe. Reintente\n")
                println("-".repeat(20))
            }

        }
    }
}