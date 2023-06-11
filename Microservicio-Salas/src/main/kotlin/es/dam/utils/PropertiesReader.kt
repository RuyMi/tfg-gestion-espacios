package es.dam.utils

import java.io.FileNotFoundException
import java.util.*

/**
 * Clase que lee un fichero de propiedades. Se le pasa el nombre del fichero y se puede obtener el valor de una propiedad.
 *
 * @property fileName Nombre del fichero de propiedades.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
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
     * Obtiene el valor de una propiedad.
     *
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
