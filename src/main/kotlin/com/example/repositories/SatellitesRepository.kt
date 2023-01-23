package com.example.repositories

import com.example.database.tables.SatelliteDAO
import com.example.database.tables.SatellitesTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class SatellitesRepository {

    fun findByNoradId(id: String) : SatelliteDAO? = transaction {
        SatelliteDAO.find { SatellitesTable.noradCatId eq id }.find { true }
    }

    fun find(id: EntityID<Int>) : SatelliteDAO?
    {
        var result : SatelliteDAO? = null
        transaction {

            result = SatelliteDAO[id]

        }

        return result
    }

    fun getAllInOrbit() : MutableList<SatelliteDAO> = transaction {
        SatelliteDAO.all().toMutableList()
    }

    fun getAllDebris() {

    }
}