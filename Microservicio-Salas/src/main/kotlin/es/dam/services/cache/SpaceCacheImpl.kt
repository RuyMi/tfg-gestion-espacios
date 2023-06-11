package es.dam.services.cache

import es.dam.models.Space
import io.github.reactivecircus.cache4k.Cache
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.*
import kotlin.time.Duration.Companion.minutes

/**
 * Clase que implementa la caché de espacios. Implementa la interfaz [SpaceCache].
 * Se encarga de almacenar los espacios en caché y de actualizarlos cada cierto tiempo.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Single
@Named("SpaceCacheImpl")
class SpaceCacheImpl : SpaceCache {
    override val hasRefreshAllCacheJob: Boolean = true
    override val refreshTime =  60 * 60 * 1000L

    override val cache = Cache.Builder()
        .expireAfterAccess(60.minutes)
        .build<UUID, Space>()
}