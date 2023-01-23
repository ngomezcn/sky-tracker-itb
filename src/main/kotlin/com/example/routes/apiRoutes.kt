package com.example.routes

import com.example.database.modelsoSatellite.UserDAO
import com.example.database.modelsoSatellite.UsersTable
import com.example.database.tables.SatelliteDAO
import com.example.database.tables.UserTrackingSatDAO
import com.example.html.AppLayout
import com.example.html.account.createAccount
import com.example.html.account.signIn
import com.example.html.account.trackingList.trackingList
import com.example.loggedUser
import com.example.models.Position
import com.example.models.n2yo.N2VisualPasses
import com.example.repositories.N2yoRepository
import com.example.repositories.SatellitesRepository
import com.example.repositories.UserRepository
import com.example.repositories.UserTrackingSatRepository
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.application.application
import io.ktor.server.html.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
@Resource("api")
class Api(){

    @Serializable
    @Resource("active_users")
    class ActiveUsers(val parent: Api = Api())
}

fun Route.api() {
    val userRepository = UserRepository()

    swaggerUI(path = application.href(Api())) {

    }

    get<Api.ActiveUsers> {
        val result = userRepository.getActiveUsers().toList()

        call.respond(result)
    }
}