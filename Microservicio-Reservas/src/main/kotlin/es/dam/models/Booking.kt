package es.dam.models

import es.dam.serializers.IdSerializer
import es.dam.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDateTime
import java.util.*

/**
 * Modelo de reserva.
 * @param id: Id de la reserva.
 * @param uuid: UUID de la reserva.
 * @param userId: UUID del usuario que ha realizado la reserva.
 * @param userName: Nombre del usuario que ha realizado la reserva.
 * @param spaceId: UUID del espacio reservado.
 * @param spaceName: Nombre del espacio reservado.
 * @param image: Imagen del espacio reservado.
 * @param startTime: Fecha y hora de inicio de la reserva.
 * @param endTime: Fecha y hora de fin de la reserva.
 * @param observations: Observaciones de la reserva.
 * @param status: Estado de la reserva.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */

@Serializable
data class Booking(
    @BsonId
    @Serializable(with = IdSerializer::class)
    val id: Id<Booking> = newId(),
    val uuid: String = UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val spaceId: String,
    val spaceName: String,
    val image: String?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime,
    val observations: String?,
    val status: Status = Status.PENDING,

) {
    enum class Status {
        PENDING, APPROVED, REJECTED
    }
}
