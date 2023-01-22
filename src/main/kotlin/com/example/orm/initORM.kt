package com.example.orm

import com.example.models.SpaceTrack.STSatelliteCatalog
import com.example.orm.tables.*
import com.example.orm.modelsoSatellite.UsersTable
import com.example.repositories.Repository
import com.example.repositories.SatellitesRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


object ORM {
    lateinit var db: Database

    fun env(a: String): String {
        return System.getenv(a)
    }

    fun connect() {

        val os = System.getProperty("os.name").lowercase()

        if(System.getenv("RAILWAY") != null)
        {
            db = Database.connect(
                "jdbc:postgresql://${env("PGHOST")}:${env("PGPORT")}/${env("PGDATABASE")}",
                driver = "com.impossibl.postgres.jdbc.PGDriver",
                user = env("PGUSER"),
                password = env("PGPASSWORD")
            )
            return
        }

        if (os.contains("windows")) {
            db = Database.connect(
                "jdbc:postgresql://localhost:5432/postgres",
                driver = "com.impossibl.postgres.jdbc.PGDriver",
                user = "postgres",
                password = "1234"
            )
            return
        }

        if (os.contains("linux")) {
            db = Database.connect(
                "jdbc:pgsql://localhost:5432/template1",
                driver = "com.impossibl.postgres.jdbc.PGDriver",
                user = "sjo",
                password = "p4ssword!"
            )
            return
        }

        db = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
    }

    fun createSchemas() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(SatellitesTable)
            SchemaUtils.create(SatCommentTable)
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(UserTrackingSatTable)
        }
    }

    fun loadInitialData() {
        transaction {
            addLogger(StdOutSqlLogger)
            if (SatelliteDAO.all().count() < 100) {
                println("DATABASE IS EMPTY, LOADING DATA")
                SatellitesTable.dropStatement()

                // Cargamos los sats desde un fichero o desde la api, por el momento lo dejo en el fichero porque son bastantes datos

                //val satellitesList = Repository().getAllSatellites()


                val file = File("sats.csv")

                BufferedReader(FileReader(file)).use { br ->
                    var line: String?
                    while (br.readLine().also { line = it } != null) {

                        val rawSat = line!!.split(",")

                        SatelliteDAO.new {
                            noradCatId = rawSat[0]
                            objectName = rawSat[1].ifEmpty { null }
                            objectID = rawSat[2].ifEmpty { null }
                            countryCode = rawSat[3].ifEmpty { null }
                            launchDate = rawSat[4].ifEmpty { null }
                            decayDate = rawSat[5].ifEmpty { null }

                            tleLine0 = rawSat[6].ifEmpty { null }
                            tleLine1 = rawSat[7].ifEmpty { null }
                            tleLine2 = rawSat[8].ifEmpty { null }
                        }
                    }
                }

                /*val str = File("sats.json").readText(Charsets.UTF_8)
                val satellitesList = Json.decodeFromString<List<STSatelliteCatalog>>(str)
                //var satellitesList = Json.decodeFromString<List<STSatelliteCatalog>>(File("sats.json").readText(Charsets.UTF_8))

                for (sat in satellitesList!!) {
                    SatelliteDAO.new {
                        noradCatId = sat.NORADCATID!!
                        objectName = sat.OBJECTNAME
                        objectID = sat.OBJECTID
                        countryCode = sat.COUNTRYCODE
                        launchDate = sat.LAUNCHDATE
                        decayDate = sat.DECAYDATE

                        tleLine0 = sat.TLELINE0
                        tleLine1 = sat.TLELINE1
                        tleLine2 = sat.TLELINE2
                    }
                }*/
            }
        }
    }
}
