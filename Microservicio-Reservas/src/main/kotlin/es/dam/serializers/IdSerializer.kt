package es.dam.serializers

import es.dam.models.Booking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.litote.kmongo.Id
import org.litote.kmongo.toId

object IdSerializer : KSerializer<Id<Booking>> {
    override val descriptor = PrimitiveSerialDescriptor("Id", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Id<Booking> {
        return decoder.decodeString().toId()
    }

    override fun serialize(encoder: Encoder, value: Id<Booking>) {
        encoder.encodeString(value.toString())
    }


}