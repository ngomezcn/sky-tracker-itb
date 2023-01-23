package com.example.repositories

import com.example.orm.tables.SatelliteDAO
import com.example.orm.tables.SatellitesTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    fun getAllInOrbit() : List<SatelliteDAO> = transaction {
        SatelliteDAO.find { SatellitesTable.decayDate eq null }.toList().sortedByDescending {

            if(it.launchDate.isNullOrEmpty()) {
                LocalDate.parse("1800-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } else
            {
                LocalDate.parse(it.launchDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        }
    }

    fun getAllDebris() {

    }
}