package com.example

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*


fun convertNumberToDate(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return format.format(date)
}


const val datePattern = "yyyy.MM.dd HH:mm"

fun epochToDate(epoch: Long): Date {
    val netDate = Date(epoch * 1000)
    return netDate
}

class JulianDay(private val epoch: Instant) {
    fun toInstant(day: Double): Instant {
        val l = day.toLong()
        return epoch
            .plus(l, ChronoUnit.DAYS)
            .plusNanos(Math.round((day - l) * NANOS_PER_DAY))
    }

    companion object {
        private const val NANOS_PER_DAY = 24.0 * 60.0 * 60.0 * 1000000000.0

        // Calculate Instants for some epochs as defined in Wikipedia.
        val REDUCED_JD = ZonedDateTime.of(1858, 11, 16, 12, 0, 0, 0, ZoneOffset.UTC).toInstant()
        val MODIFIED_JD = ZonedDateTime.of(1858, 11, 17, 0, 0, 0, 0, ZoneOffset.UTC).toInstant()
        val JULIAN_DATE = REDUCED_JD.minus(2400000, ChronoUnit.DAYS)
    }
}