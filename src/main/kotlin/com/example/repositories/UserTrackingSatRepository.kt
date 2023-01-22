package com.example.repositories

import com.example.orm.tables.UserTrackingSatDAO
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserTrackingSatRepository {

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