package es.dam.exceptions


/**
 * Excepción para personalizar las que tienen que ver con las reservas.
 *
 * @param message mensaje de error.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
class BookingException(message: String) : RuntimeException(message)
