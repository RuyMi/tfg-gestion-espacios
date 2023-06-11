package es.dam.utils

import java.io.FileNotFoundException
import java.util.*


/**
 * Clase que lee un fichero de propiedades.
 * @param fileName Nombre del fichero.
 *
 * @autor Alejandro Sánchez Monzón
 * @autor Rubén García-Redondo Marín
 * @autor Mireya Sánchez Pinzón
 */
class PropertiesReader(private val fileName: String) {
    private val properties = Properties()

    init {
        val file = this::class.java.classLoader.getResourceAsStream(fileName)
        if (file != null) {
            properties.load(file)
        } else {
            throw FileNotFoundException("File not found: $fileName")
        }
    }

    /**
     * Obtiene una propiedad del fichero.
     * @param key Clave de la propiedad.
     * @return Valor de la propiedad.
     */
    fun getProperty(key: String): String {
        val value = properties.getProperty(key)
        if (value != null) {
            return value
        } else {
            throw FileNotFoundException("Property $key not found in file: $fileName")
        }
    }
}
