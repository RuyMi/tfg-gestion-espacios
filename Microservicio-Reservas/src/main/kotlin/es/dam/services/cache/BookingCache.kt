package es.dam.services.cache

import es.dam.models.Booking
import java.util.*

interface BookingCache : ICache<UUID, Booking>