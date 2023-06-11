package es.dam.services.cache

import io.github.reactivecircus.cache4k.Cache

/**
 * Interfaz de la cache.
 * @param ID Clave de la cache.
 * @param T Valor de la cache.
 */
interface ICache<ID : Any, T : Any> {
    val hasRefreshAllCacheJob: Boolean
    val refreshTime: Long
    val cache: Cache<ID, T>
}