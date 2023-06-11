package es.dam.services.cache

import es.dam.models.Booking
import java.util.*

/**
 * Interfaz de la cache de reservas.
 * @param UUID Clave de la cache.
 * @param Booking Valor de la cache.
 *
 * @author Mireya Sánchez Pinzón
 * @autor Alejandro Sánchez Monzón
 * @autor Rubén García-Redondo Marín
 */
interface BookingCache : ICache<UUID, Booking>