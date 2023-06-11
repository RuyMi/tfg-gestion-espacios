package es.dam.db

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import es.dam.utils.PropertiesReader
import org.bson.UuidRepresentation
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.LoggerFactory

/**
 * Clase que gestiona la conexión con la base de datos MongoDB
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
object MongoDbManager {
    private val properties = PropertiesReader("application.properties")
    private lateinit var mongoDbClient: CoroutineClient
    lateinit var database: CoroutineDatabase

    private val STRING_CONNECTION = properties.getProperty("string_connection")

    init {
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        val rootLogger = loggerContext.getLogger("org.mongodb.driver")
        rootLogger.level = Level.ERROR
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(STRING_CONNECTION))
            .uuidRepresentation(UuidRepresentation.JAVA_LEGACY).build()
        mongoDbClient = KMongo.createClient(clientSettings).coroutine
        database = mongoDbClient.getDatabase("reservas-luisvives")
    }

}