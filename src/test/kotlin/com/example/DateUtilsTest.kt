package com.example

import io.ktor.server.util.*
import org.junit.Assert.*
import org.junit.Test
import java.util.*


class DateUtilsTest {

    private val dateUtils = DateUtils()
    @Test
    fun getDatePattern() {
        val expected = "yyyy.MM.dd HH:mm"
        assertEquals(expected, dateUtils.datePattern)
    }

    @Test
    fun RJDToDate()
    {
        val result : String = DateUtils(DateUtils.REDUCED_JD).toInstant(57273.05625).toString()
        val expected = "2015-09-07T13:21:00.000000126Z"

        assertEquals(expected, result)
    }

    @Test
    fun MJDToDate()
    {
        val result : String = DateUtils(DateUtils.MODIFIED_JD).toInstant(57273.05625).toString()
        val expected = "2015-09-08T01:21:00.000000126Z"

        assertEquals(expected, result)
    }

    @Test
    fun epochToDate() {
        val expected = "Mon Jan 23 12:26:05 CET 2023"
        assertEquals(expected, dateUtils.epochToDate(1674473165).toString())
    }
}