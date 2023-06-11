package es.dam.services.cache

import es.dam.models.Space
import java.util.*

/**
 * Interfaz que define las operaciones de un caché de espacios. Implementa la interfaz [ICache].
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

interface SpaceCache : ICache<UUID, Space>