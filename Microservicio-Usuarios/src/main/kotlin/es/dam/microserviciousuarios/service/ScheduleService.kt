package es.dam.microserviciousuarios.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduleService @Autowired constructor(
    val userService: UserService
){
    @Scheduled(cron = "0 0 1 1 * ?")
    suspend fun poner20creditosAllUsers() {
        println("Poniendo 20 creditos a todos los usuarios")
        userService.findAll().forEach {
            //it.credits = 20
            //userService.save(it)
        }
    }
}