package com.example.plugins

import com.example.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.resources.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {
        static("/") {
            resources("static")
        }

        rootRoutes()
        accountRoutes()
        api()
        satelliteRoutes()
    }
}
