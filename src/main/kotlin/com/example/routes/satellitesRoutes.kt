package com.example.routes

import com.example.html.AppLayout
import com.example.html.satellite.satelliteDetail
import com.example.loggedUser
import com.example.database.tables.SatCommentDAO
import com.example.database.tables.SatelliteDAO
import com.example.database.tables.SatellitesTable
import com.example.pathAssetsSats
import com.example.repositories.SatCommentRepository
import com.example.repositories.SatellitesRepository
import com.example.repositories.UserTrackingSatRepository
import com.example.templates.content.satellitesList
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import io.ktor.server.resources.post
import org.jetbrains.exposed.sql.select
import java.io.File
import java.nio.file.Files

@Serializable
@Resource("satellites")
class Satellites(val page : Int? = null, val itemsPerPage : Int? = null, val filter : String? = null){
    @Serializable
    @Resource("{noradId}")
    class NoradId(val parent: Satellites = Satellites(), val noradId: String) {

    }
    @Serializable
    @Resource("comment")
    class Comment(val parent: Satellites = Satellites())
}

fun contains(str: String?, toContain: String) : Boolean {
    if(str == null)
        return false

    return str.uppercase().contains(toContain.uppercase())
}

fun Route.satelliteRoutes() {

    val satellitesRepository = SatellitesRepository()
    val satCommentRepository = SatCommentRepository()
    val userTrackingSatRepository = UserTrackingSatRepository()

    get<Satellites> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))

        var page : Int? = call.request.queryParameters["page"]?.toInt()
        var itemsPerPage : Int? = call.request.queryParameters["itemsPerPage"]?.toInt()
        var filter : String? = call.request.queryParameters["filter"]

        if(page == null)
            page = 1
        if(itemsPerPage == null)
            itemsPerPage = 5000

        var sats : MutableList<SatelliteDAO> = satellitesRepository.getAllInOrbit().sortedBy {
            it.launchDate
        }.toMutableList()
        sats.removeIf { it.launchDate == null || it.decayDate != null }

        when(filter){
            "noDebris" -> sats.removeIf { it.objectName?.uppercase()?.contains("DEB") ?: true }
            "debris" -> sats.retainAll { it.objectName?.uppercase()?.contains("DEB") ?: false }
            "starlink" -> sats.retainAll { it.objectName?.uppercase()?.contains("STAR") ?: false }
        }

        var toSlice : IntRange = ((page * itemsPerPage).toInt()..(page * itemsPerPage+itemsPerPage).toInt())
        if (toSlice.last > sats.size)
        {
            toSlice = (toSlice.first until sats.size)
        }
        var toList = sats.toList().slice(toSlice)

        call.respondHtmlTemplate(AppLayout(application)) {

            content{
                transaction {
                    satellitesList(application, toList, page, itemsPerPage, filter)
                }
            }
        }
    }

    get<Satellites.NoradId> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))

        val id : String = if (call.parameters["noradId"].isNullOrBlank()) "" else call.parameters["noradId"]!!
        val sat = satellitesRepository.findByNoradId(id)

        val trackedSat = userTrackingSatRepository.findSatTrackedByUser(satId = sat!!.id, userId = loggedUser!!.id)

        if(sat == null) {
            call.respondRedirect(application.href(Satellites()))
        } else
        {
            val comments = satCommentRepository.getAllBySatellite(sat)

            call.respondHtmlTemplate(AppLayout(application)) {

                val a = call.request.origin

                content{
                    transaction {
                        satelliteDetail(sat, comments, trackedSat != null , application)
                    }
                }
            }
        }
    }

    post<Satellites.Comment> {
        if(loggedUser == null)
            call.respondRedirect(application.href(Account.SignIn()))

        val data = call.receiveMultipart()
        var _idSatellite = ""
        var _comment = ""
        var _imageName = ""

        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name){
                        "idSatellite" -> _idSatellite = part.value
                        "message" -> _comment = part.value
                    }
                }
                is PartData.FileItem -> {
                    val fileBytes = part.streamProvider().readBytes()

                    if(fileBytes.isNotEmpty())
                    {
                        val uuid: UUID = UUID.randomUUID()
                        _imageName =  loggedUser!!.email.take(4)+"-"+uuid.toString()+"."+part.originalFileName!!.substringAfterLast('.', "")
                        Files.createDirectories(pathAssetsSats.toPath());
                        File("$pathAssetsSats/$_imageName").writeBytes(fileBytes)
                    }
                }
                else -> {
                }
            }
        }

        if(_comment.isEmpty()) {
            call.respondText("Comment is empty", status = HttpStatusCode.NotFound)
        }

        var satToComment: SatelliteDAO? = null
        transaction {
            val query = SatellitesTable.select {
                SatellitesTable.id eq _idSatellite.toInt()
            }

            if (SatelliteDAO.wrapRows(query).count().toInt() > 0) {
                satToComment = SatelliteDAO.wrapRows(query).toList().first()
            }
        }

        if(satToComment == null) {
            call.respondText("Satellite does not exist", status = HttpStatusCode.NotFound)
        }

        transaction {
            SatCommentDAO.new {
                idSatellite = satToComment!!
                idUser = loggedUser!!
                comment = _comment
                imageName = _imageName
                username = loggedUser!!.username
                upVotes = 0
            }
        }

        call.respondRedirect(application.href(Satellites.NoradId(noradId = satToComment!!.noradCatId)))
    }
}