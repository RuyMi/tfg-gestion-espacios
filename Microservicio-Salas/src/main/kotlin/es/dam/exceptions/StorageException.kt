package es.dam.exceptions

/**
 * Clase personalizada de excepción para la clase Storage
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
sealed class StorageException(message: String) : RuntimeException(message) {
    class FileNotFound(message: String) : StorageException(message)
    class FileNotSave(message: String) : StorageException(message)
}