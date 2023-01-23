package com.example.repositories

import com.example.orm.tables.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserTrackingSatRepository {

    fun allSatsTrackedByUser(idUser : EntityID<Int>) : List<SatelliteDAO> {

        //val query : List<SatelliteDAO> = UserTrackingSatDAO.find { UserTrackingSatTable.idUser eq 8 }.toList()
        var sats: List<SatelliteDAO> = listOf()
        transaction {
            addLogger(StdOutSqlLogger)

            val query = SatellitesTable.innerJoin(UserTrackingSatTable)
                .slice(SatellitesTable.columns)
                .select {
                    (SatellitesTable.id eq UserTrackingSatTable.idSatellite) and (UserTrackingSatTable.idUser eq idUser)
                }.withDistinct()

            if (SatelliteDAO.wrapRows(query).count().toInt() > 0) {
                sats = SatelliteDAO.wrapRows(query).toList()
            }
        }

        return sats
    }


    fun allByUser(idUser : EntityID<Int>) : List<UserTrackingSatDAO> = transaction {

        UserTrackingSatDAO.find { UserTrackingSatTable.idUser eq idUser }.toList()
    }


    fun findSatTrackedByUser(userId: EntityID<Int>, satId: EntityID<Int>) : UserTrackingSatDAO?
    {
        var result : UserTrackingSatDAO? = null

        transaction {
            addLogger(StdOutSqlLogger)

            result = UserTrackingSatDAO.all().find { it.idUser == userId && it.idSatellite == satId}
        }

        return result
    }
}