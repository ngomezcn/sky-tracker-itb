package com.example.database.tables

import com.example.database.modelsoSatellite.UserDAO
import com.example.database.modelsoSatellite.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object SatCommentTable : IntIdTable() {
    //val idSatellite: Column<EntityID<Int>> = entityId("idSatellite", SatellitesTable)
    val idSatellite = reference("idSatellite", SatellitesTable)
    val idUser = reference("idUser", UsersTable)
    val username : Column<String> = varchar("username", 50)

    val comment: Column<String> = varchar("message", 1000)
    val imageName: Column<String> = varchar("imageName", 50)
    val upVotes: Column<Int> = integer("upVotes").default(0)
}

class SatCommentDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SatCommentDAO>(SatCommentTable)
    var idSatellite by SatelliteDAO referencedOn SatCommentTable.idSatellite
    var idUser by UserDAO referencedOn SatCommentTable.idUser
    var username by SatCommentTable.username

    var comment by SatCommentTable.comment
    var imageName by SatCommentTable.imageName
    var upVotes by SatCommentTable.upVotes
}
