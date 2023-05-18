package es.dam.models

import es.dam.serializers.IdSerializer
import es.dam.serializers.LocalDateTimeSerializer
import es.dam.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDateTime
import java.util.*

//TODO Poner nombre de usuario y nombre del espacio en la reserva
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
    @Serializable(with = LocalDateTimeSerializer::class)
    val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime,
    val phone: String?,
    val status: Status = Status.PENDING,

) {
    enum class Status {
        PENDING, APPROVED, REJECTED
    }
}
