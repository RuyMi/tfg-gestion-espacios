package es.dam.models

import es.dam.serializers.IdSerializer
import es.dam.serializers.UUIDSerializer
import kotlinx.serialization.Contextual
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
    val bookingWindow: String
) {
    enum class UserRole {
        ADMINISTRATOR, TEACHER, USER
    }
}