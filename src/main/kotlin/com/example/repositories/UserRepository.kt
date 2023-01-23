package com.example.repositories

import com.example.database.modelsoSatellite.UsersTable
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun getActiveUsers(): List<ResultRow> {

        var result: Query? = null

        transaction {
            result = UsersTable
                .slice(UsersTable.id.count(), UsersTable.email)
                .selectAll()
                .groupBy(UsersTable.email)
        }

        return result?.toList() ?: listOf()
    }

}