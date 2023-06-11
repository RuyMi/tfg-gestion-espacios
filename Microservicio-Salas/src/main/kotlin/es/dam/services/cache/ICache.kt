package es.dam.services.cache

import io.github.reactivecircus.cache4k.Cache

/**
 * Interfaz que define las operaciones de un caché.
 * Define las operaciones de un caché, como obtener el tiempo de refresco, si tiene un trabajo de refresco
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
interface ICache<ID : Any, T : Any> {
    val hasRefreshAllCacheJob: Boolean
    val refreshTime: Long
    val cache: Cache<ID, T>
}