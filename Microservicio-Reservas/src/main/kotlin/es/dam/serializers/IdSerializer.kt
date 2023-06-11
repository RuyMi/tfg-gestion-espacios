package es.dam.serializers

import es.dam.models.Booking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.litote.kmongo.Id
import org.litote.kmongo.toId

/**
 * Serializador de Id<Booking>.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
object IdSerializer : KSerializer<Id<Booking>> {
    override val descriptor = PrimitiveSerialDescriptor("Id", PrimitiveKind.STRING)

    /**
     * Deserializa un Id<Booking>.
     * @param decoder Decoder
     * @return Id<Booking>
     */
    override fun deserialize(decoder: Decoder): Id<Booking> {
        return decoder.decodeString().toId()
    }

    /**
     * Serializa un Id<Booking>.
     * @param encoder Encoder
     * @param value Id<Booking>
     */
    override fun serialize(encoder: Encoder, value: Id<Booking>) {
        encoder.encodeString(value.toString())
    }


}