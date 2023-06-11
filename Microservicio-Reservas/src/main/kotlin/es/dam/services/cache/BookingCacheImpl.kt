package es.dam.services.cache

import es.dam.models.Booking
import io.github.reactivecircus.cache4k.Cache
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*
import kotlin.time.Duration.Companion.minutes

/**
 * Cache de reservas.
 *
 * @author Mireya Sánchez Pinzón
 * @autor Alejandro Sánchez Monzón
 * @autor Rubén García-Redondo Marín
 */
@Single
@Named("BookingCacheImpl")
class BookingCacheImpl : BookingCache {
    override val hasRefreshAllCacheJob: Boolean = true
    override val refreshTime =  60 * 60 * 1000L

    override val cache = Cache.Builder()
        .expireAfterAccess(60.minutes)
        .build<UUID, Booking>()
}