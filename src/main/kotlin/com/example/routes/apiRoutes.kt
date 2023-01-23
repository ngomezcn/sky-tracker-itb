package com.example.routes

import com.example.database.modelsoSatellite.UsersTable
import com.example.database.tables.UserTrackingSatTable
import com.example.repositories.UserRepository
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.count

@Serializable
@Resource("api")
class Api(){

    @Serializable
    @Resource("sats_by_user")
    class SatsByUser(val parent: Api = Api())
}

fun Route.api() {
    val userRepository = UserRepository()

    swaggerUI(path = application.href(Api())) {

    }

    get<Api.SatsByUser> {
        val result = userRepository.groupByUsersTracking()

        /*result!!.map{ row ->
                    println("""
                user = ${row[UsersTable.email]},
                tracking = ${row[UserTrackingSatTable.id.count()] ?: 0},""")
                }*/

        call.respond(result)
    }
}