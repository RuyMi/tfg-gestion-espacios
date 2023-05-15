package es.dam.microserviciousuarios.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduleService {
    @Scheduled(cron = "0 0 1 1 * ?")
     fun poner20creditosAllUsers() {
        println("Poniendo 20 creditos a todos los usuarios")

    }
}