package com.example.database

import com.example.database.tables.*
import com.example.database.modelsoSatellite.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


class DatabaseManager {
    lateinit var db: Database
    init {
        connect()
        createSchemas()
        loadInitialData()
    }

    private fun env(a: String): String {
        return System.getenv(a)
    }

    private fun connect() {

        val os = System.getProperty("os.name").lowercase()
        //db = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        //return

        if(System.getenv("RAILWAY") != null)
        {
            db = Database.connect(
                "pgsql://${env("PGHOST")}:${env("PGPORT")}/${env("PGDATABASE")}",
                driver = "org.postgresql.Driver", //driver = "com.impossibl.postgres.jdbc.PGDriver",
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

    private fun createSchemas() {
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(SatellitesTable)
            SchemaUtils.create(SatCommentTable)
            SchemaUtils.create(UsersTable)
            SchemaUtils.create(UserTrackingSatTable)
        }
    }

    private fun loadInitialData() {
        transaction {
            addLogger(StdOutSqlLogger)
            if (SatelliteDAO.all().count() < 100) {
                println("DATABASE IS EMPTY, LOADING DATA")
                SatellitesTable.dropStatement()

                //val satellitesList = Repository().getAllSatellites() // Evitar cargar los datos desde la API

                // Cargar datos desde .csv linea por linea
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

                // Carga los datos, pero es muy lento y puede llegar a desbordar el heap
                /*val str = File("sats.json").readText(Charsets.UTF_8)
                val satellitesList = Json.decodeFromString<List<STSatelliteCatalog>>(str)
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
