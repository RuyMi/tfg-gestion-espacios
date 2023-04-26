package es.dam.db

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import es.dam.utils.PropertiesReader
import org.bson.UuidRepresentation
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


object MongoDbManager {
    private val properties = PropertiesReader("application.properties")
    private lateinit var mongoDbClient: CoroutineClient
    lateinit var database: CoroutineDatabase

    private val STRING_CONNECTION = properties.getProperty("string_connection")

    init {
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(STRING_CONNECTION))
            .uuidRepresentation(UuidRepresentation.JAVA_LEGACY).build()
        mongoDbClient = KMongo.createClient(clientSettings).coroutine
        database = mongoDbClient.getDatabase("reservas-luisvives")
    }

}