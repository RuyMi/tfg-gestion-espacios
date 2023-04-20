package es.dam.models

import es.dam.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.LocalDateTime
import java.util.*

data class Booking(
    @BsonId
    val id: Id<Booking> = newId(),
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    val userId: String,
    val spaceId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val phone: String?,
    val status: Status = Status.PENDING,

) {
    enum class Status {
        PENDING, APPROVED, REJECTED
    }
}
