package com.example

import com.example.database.DatabaseManager
import com.example.database.modelsoSatellite.UserDAO
import com.example.database.modelsoSatellite.UsersTable

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Paths
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*

var loggedUser : UserDAO? = null

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

    val dm = DatabaseManager()

    // ESTO ES SOLO TEMPORAL DURANTE TESTING
    /*transaction {
        addLogger(StdOutSqlLogger)

        val query = UsersTable.select {
            (UsersTable.email eq "naimgomezcn@gmail.com") and (UsersTable.password eq "1234")
        }

        if(UserDAO.wrapRows(query).count().toInt() > 0) {
            loggedUser =  UserDAO.wrapRows(query).toList().first()
        }
    }*/
    // === == == == == =

    install(CORS)
    install(Resources)
    configureSerialization()
    configureRouting()
}


/*fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}*/

val basePath  = File(DatabaseManager::class.java.protectionDomain.codeSource.location.path).path!!
val pathAssetsSats = Paths.get(basePath).parent.parent.parent.combineSafe(Paths.get("resources/main/static/assets/sats"))
