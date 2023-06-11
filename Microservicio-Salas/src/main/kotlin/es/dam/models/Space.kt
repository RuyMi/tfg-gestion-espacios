package es.dam.models

import es.dam.serializers.IdSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

/**
 * Clase Space. Representa un espacio de la aplicación.
 *
 * @property id Identificador del espacio en la base de datos
 * @property uuid  Identificador único del espacio en la aplicación
 * @property name Nombre del espacio
 * @property description Descripción del espacio (opcional)
 * @property image Imagen del espacio (opcional)
 * @property price Precio del espacio
 * @property isReservable Indica si el espacio es reservable o no
 * @property requiresAuthorization Indica si el espacio requiere autorización para ser reservado
 * @property authorizedRoles Roles autorizados para reservar el espacio
 * @property bookingWindow Ventana de reserva del espacio (en días)
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Serializable
data class Space(
    @BsonId
    @Serializable(with = IdSerializer::class)
    val id: Id<Space> = newId(),
    val uuid: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = "",
    val image: String? = null,
    val price: Int,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val authorizedRoles: Set<UserRole>,
    val bookingWindow: Int
) {
    enum class UserRole {
        ADMINISTRATOR, TEACHER, USER
    }
}