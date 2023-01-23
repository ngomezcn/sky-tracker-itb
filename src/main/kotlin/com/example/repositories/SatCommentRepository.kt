package com.example.repositories

import com.example.database.modelsoSatellite.UserDAO
import com.example.database.tables.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class SatCommentRepository {

    fun getAllBySatellite(satellite: SatelliteDAO) : List<SatCommentDAO>  {

         var comments: List<SatCommentDAO> = listOf()
         transaction {

             val query = SatCommentTable.innerJoin(SatellitesTable)
                 .slice(SatCommentTable.columns)
                 .select {
                     (SatellitesTable.id eq SatCommentTable.idSatellite) and (SatellitesTable.id eq satellite.id)
                 }.withDistinct()

             if (SatCommentDAO.wrapRows(query).count().toInt() > 0) {
                 comments = SatCommentDAO.wrapRows(query).toList()
             }
         }

         return comments
    }
}