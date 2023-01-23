package com.example.routes

import com.example.html.AppLayout
import com.example.html.account.createAccount
import com.example.html.account.signIn
import com.example.html.account.trackingList.trackingList
import com.example.loggedUser
import com.example.models.Position
import com.example.models.n2yo.N2VisualPasses
import com.example.database.modelsoSatellite.UserDAO
import com.example.database.modelsoSatellite.UsersTable
import com.example.database.tables.SatelliteDAO
import com.example.database.tables.UserTrackingSatDAO
import com.example.repositories.N2yoRepository
import com.example.repositories.SatellitesRepository
import com.example.repositories.UserTrackingSatRepository
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
import org.jetbrains.exposed.sql.*

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

    @Serializable
    @Resource("untrack_sat")
    class UntrackSat(val parent: Account = Account())

    @Serializable
    @Resource("active_users")
    class ActiveUsers(val parent: Account = Account())
}

fun Route.accountRoutes() {
    val userTrackingSatRepository = UserTrackingSatRepository()
    val restRepository = N2yoRepository()

    get<Account.TrackingList> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))
        val visualPasses : MutableList<N2VisualPasses> = mutableListOf()

        val trackedSats = userTrackingSatRepository.allByUser(loggedUser!!.id)

        for(track in trackedSats) {

            val sat = SatellitesRepository().find(track.idSatellite)


            val pass = restRepository.getVisualPasses(sat!!.noradCatId, Position(track.latitude, track.longitude, 0.0F))

            visualPasses.add(pass)
        }
        
        call.respondHtmlTemplate(AppLayout(application)) {
            content{
                transaction {  
                    trackingList(application, visualPasses)
                }
            }
        }
    }

    post<Account.TrackingList> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))

        val data = call.receiveMultipart()
        var _idSatellite = 0
        var _latitude : Float = 0.0F
        var _longitude : Float = 0.0F



        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name){
                        "idSatellite" -> _idSatellite = part.value.toInt()
                        //"latitude" -> _latitude = part.value.toFloat()
                        //"longitude" -> _longitude = part.value.toFloat()
                        "position"-> {
                            _latitude = part.value.split(",")[0].toFloat()
                            _longitude = part.value.split(",")[1].toFloat()
                        }
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
                latitude = _latitude
                longitude = _longitude
            }
        }

        call.respondRedirect(application.href(Satellites.NoradId(noradId = oSatellite!!.noradCatId)))
    }

    post<Account.UntrackSat> {
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
            call.respond("Satellite not found")
        }
        val oTrack : UserTrackingSatDAO? = userTrackingSatRepository.findSatTrackedByUser(loggedUser!!.id, oSatellite!!.id)

        transaction {
            oTrack!!.delete()
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

    get<Account.ActiveUsers> {
        val a  = UsersTable
            .slice(UsersTable.id.count(), UsersTable.email)
            .selectAll()
            .groupBy(UsersTable.email).toList()
        call.respond(a)
    }
}
