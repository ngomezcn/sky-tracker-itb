package com.example.plugins

import com.example.routes.Root
import com.example.routes.accountRoutes
import com.example.routes.rootRoutes
import com.example.routes.satelliteRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {
        println("Request")

        get("/") {
            call.respondRedirect(application.href(Root.Home()))
        }

        //resource("/", "index.html")
        //resource("*", "index.html")

        static("/") {
            resources("static")
        }

        //openAPI(path="openapi", swaggerFile = "D:\\GitRepos\\SkyTracker_\\src\\main\\resources\\static\\openapi/documentation.yaml")
        //swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        //apiRouting()
        //webRouting()

        // New routing
        rootRoutes()
        accountRoutes()
        satelliteRoutes()
    }
}
