package es.dam.microserviciousuarios.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Clase de servicio de la planificacion de creditos. Se encarga de planificar la operacion de poner 20 creditos a todos los usuarios todos los meses.
 *
 * @author Mireya Sánchez Pinzón
 * @author Alejandro Sánchez Monzón
 * @author Rubén García-Redondo Marín
 */
@Component
class ScheduleService constructor(
    @Autowired
    val userService: UserService){
    @Scheduled(cron = "0 0 1 1 * ?")
     fun poner20creditosAllUsers() {
         println("${LocalDateTime.now()} INFO --- [Schedule credits function] ScheduleService : Poniendo 20 creditos a todos los usuarios...")
         userService.poner20creditosAllUsers();
         println("${LocalDateTime.now()} INFO --- [Schedule credits function] ScheduleService : Operacion completada")
    }
}