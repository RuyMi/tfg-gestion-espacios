package es.dam.exceptions

sealed class StorageException(message: String) : RuntimeException(message) {
    class FileNotFound(message: String) : StorageException(message)
    class FileNotSave(message: String) : StorageException(message)
}