package com.example.repositories

import com.example.database.modelsoSatellite.UsersTable
import com.example.database.tables.UserTrackingSatTable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun groupByUsersTracking(): MutableMap<String, String>  {

        val result : MutableMap<String, String> = mutableMapOf()
        transaction {
            addLogger(StdOutSqlLogger)
            val query = UsersTable
                .innerJoin(UserTrackingSatTable)
                .slice(UsersTable.email, UserTrackingSatTable.id.count())
                .selectAll()
                .groupBy(UsersTable.email)

            query.map {
                result.set(it[UsersTable.email],
                    it[UserTrackingSatTable.id.count()].toString() )
            }
        }
        return result
    }
}