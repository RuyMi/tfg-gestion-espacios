package es.dam.models

import es.dam.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.id.toId
import org.litote.kmongo.newId
import java.util.*
import kotlin.time.Duration

@Serializable
data class Space(
    @BsonId
    val id: Id<Space> = newId(),
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    val name: String,
    val isReservable: Boolean,
    val requiresAuthorization: Boolean,
    val maxBookings: Int?,
    val authorizedRoles: Set<UserRole>,
    val bookingWindow: Duration
) {
    enum class UserRole {
        ADMINISTRATOR, TEACHER, USER
    }
}