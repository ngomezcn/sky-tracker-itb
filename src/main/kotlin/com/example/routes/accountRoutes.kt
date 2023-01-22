package com.example.routes

import com.example.html.AppLayout
import com.example.html.account.createAccount
import com.example.html.account.signIn
import com.example.html.account.trackingList
import com.example.loggedUser
import com.example.orm.modelsoSatellite.UserDAO
import com.example.orm.modelsoSatellite.UsersTable
import com.example.orm.tables.SatelliteDAO
import com.example.orm.tables.UserTrackingSatDAO
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*

import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.serialization.Serializable
import io.ktor.server.resources.post
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

@Serializable
@Resource("account")
class Account(){
    @Serializable
    @Resource("tracking_list")
    class TrackingList(val parent: Account = Account())

    @Serializable
    @Resource("sign_in")
    class SignIn(val parent: Account = Account())

    @Serializable
    @Resource("create_account")
    class CreateAccount(val parent: Account = Account())

    @Serializable
    @Resource("logout")
    class Logout(val parent: Account = Account())
}

fun Route.accountRoutes() {
    //val accountRepository = FilmsRepository()

    get<Account.TrackingList> {
        call.respondHtmlTemplate(AppLayout(application)) {
            content{
                transaction {
                    trackingList()
                }
            }
        }
    }

    post<Account.TrackingList> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))

        val data = call.receiveMultipart()
        var _idSatellite = 0

        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name){
                        "idSatellite" -> _idSatellite = part.value.toInt()
                    }
                }
                is PartData.FileItem -> {
                }
                else -> {
                }
            }
        }

        val oSatellite: SatelliteDAO? = SatelliteDAO.getSatellite(_idSatellite)

        if(oSatellite == null)
        {
            call.respond("Not found")
        }

        transaction {
            UserTrackingSatDAO.new {
                idSatellite = oSatellite!!.id
                idUser = loggedUser!!.id
            }
        }

        call.respondRedirect(application.href(Satellites.NoradId(noradId = oSatellite!!.noradCatId)))
    }

    delete<Account.TrackingList> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))

        val data = call.receiveMultipart()
        var _idSatellite = 0

        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name){
                        "idSatellite" -> _idSatellite = part.value.toInt()
                    }
                }
                is PartData.FileItem -> {
                }
                else -> {
                }
            }
        }

        val oSatellite: SatelliteDAO? = SatelliteDAO.getSatellite(_idSatellite)

        if(oSatellite == null)
        {
            call.respond("Not found")
        }
        transaction {
            oSatellite!!.delete()
        }

        call.respondRedirect(application.href(Satellites.NoradId(noradId = oSatellite!!.noradCatId)))
    }

    get<Account.SignIn> {
        if(loggedUser != null)
            call.respondRedirect(application.href(Account.TrackingList()))

        call.respondHtmlTemplate(AppLayout(application)) {
            content{
                transaction {
                    signIn(application)
                }
            }
        }
    }

    post<Account.SignIn> {

        val data = call.receiveMultipart()

        var email = ""
        var password = ""

        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    transaction {
                        when (part.name){
                            "email" -> email = part.value
                            "password" -> password = part.value
                        }
                    }
                }
                is PartData.FileItem -> {                    }
                else -> {
                }
            }
        }

        transaction {
            addLogger(StdOutSqlLogger)

            val query = UsersTable.select {
                (UsersTable.email eq email) and (UsersTable.password eq password)
            }

            if(UserDAO.wrapRows(query).count().toInt() > 0) {
                loggedUser =  UserDAO.wrapRows(query).toList().first()
            }
        }

        if(loggedUser == null) {
            call.respondRedirect(application.href(Account.SignIn()) /*"/sign_in?errorMsg=\"Datos incorrectos\""*/)
        } else
        {
            call.respondRedirect(application.href(Satellites()))
        }
    }

    get<Account.CreateAccount> {
        if(loggedUser != null)
            call.respondRedirect(application.href(Account.TrackingList()))

        call.respondHtmlTemplate(AppLayout(application)) {
            content{
                transaction {
                    createAccount(application)
                }
            }
        }
    }

    post<Account.CreateAccount> {
        val data = call.receiveMultipart()

        var _username = ""
        var _firstName = ""
        var _surname = ""
        var _email = ""
        var _password = ""

        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    transaction {
                        when (part.name){
                            "username" -> _username = part.value
                            "firstName" -> _firstName = part.value
                            "surname" -> _surname = part.value
                            "email" -> _email = part.value
                            "password" -> _password = part.value
                        }
                    }
                }
                is PartData.FileItem -> {

                }
                else -> {
                }
            }}

        var user : UserDAO? = null
        try {
            transaction {
                user = UserDAO.new {
                    email = _email
                    username = _username
                    firstName = _firstName
                    surname = _surname
                    password = _password
                    sessionCookie = ""
                }
            }
        } catch (e : Exception) {

            call.respondRedirect(application.href(Account.CreateAccount()))
            throw e
        }

        loggedUser = user
        call.respondRedirect(application.href(Satellites()))
    }

    get<Account.Logout> {
        loggedUser = null
        call.respondRedirect(application.href(Account.SignIn()))
    }
}
