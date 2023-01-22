package com.example.orm.tables

import com.example.orm.modelsoSatellite.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

object UserTrackingSatTable : IntIdTable() {

    val idUser = reference("idUser", UsersTable)
    val idSatellite = reference("idSatellite", SatellitesTable)
    val trackingDate = datetime("trackingDate").defaultExpression(CurrentDateTime)
}

class UserTrackingSatDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserTrackingSatDAO>(UserTrackingSatTable){

    }

    var idUser by UserTrackingSatTable.idUser
    var idSatellite by UserTrackingSatTable.idSatellite
    var trackingDate by UserTrackingSatTable.trackingDate
}
