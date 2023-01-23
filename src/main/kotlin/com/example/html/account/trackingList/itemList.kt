package com.example.html.account.trackingList

import com.example.DateUtils
import com.example.models.n2yo.N2VisualPasses
import com.example.routes.Account
import com.example.routes.Satellites
import io.ktor.server.application.*
import io.ktor.server.resources.*
import kotlinx.html.*
import java.text.DateFormatSymbols

fun FlowContent.itemList(application:Application, pass: N2VisualPasses) {

    div("accordion-item") {
        h3("accordion-header") {
            id = "headingThree"
            button(classes = "accordion-button collapsed") {
                type = ButtonType.button
                attributes["data-bs-toggle"] = "collapse"
                attributes["data-bs-target"] = "#collapse${pass.hashCode()}"
                attributes["aria-expanded"] = "false"
                attributes["aria-controls"] = "collapse${pass.hashCode()}"
                +if (pass.info?.satname == null) "unnamed" else pass.info!!.satname!!
            }
        }
        div("accordion-collapse collapse") {
            id = "collapse${pass.hashCode()}"
            attributes["aria-labelledby"] = "headingThree"
            attributes["data-bs-parent"] = "#accordionExample"
            div("accordion-body") {
                if (pass.passes.size > 0) {
                    table("table table-bordered table-striped thead-dark") {

                        thead {
                            tr {
                                th {
                                    rowSpan = "2"
                                    +"""Date"""
                                }
                                th {
                                    colSpan = "3"
                                    +"""Inicio"""
                                }
                                th {
                                    colSpan = "3"
                                    +"""Highest point"""
                                }
                                th {
                                    colSpan = "3"
                                    +"""End"""
                                }
                            }
                            tr {
                                td { +"""Hour""" } // startUTC
                                td { +"""Ele.""" } // startEl
                                td { +"""Com.""" }  // startAzCompass
                                td { +"""Hour""" }
                                td { +"""Ele.""" }
                                td { +"""Com.""" }
                                td { +"""Hour""" }
                                td { +"""Ele.""" }
                                td { +"""Com.""" }
                            }
                        }
                        tbody {

                            for (line in pass.passes) {

                                val dateUtils = DateUtils()

                                val oStartDate = dateUtils.epochToDate(line.startUTC!!.toLong())
                                val oMaxDate = dateUtils.epochToDate(line.maxUTC!!.toLong())
                                val oEndDate = dateUtils.epochToDate(line.endUTC!!.toLong())

                                tr("clickableRow") {

                                    td { +if (line.startUTC == null) "-" else "${oStartDate.day} of ${DateFormatSymbols().months[oStartDate.month]}" }
                                    td { +if (line.startUTC == null) "-" else "${oStartDate.hours}:${oStartDate.minutes}" }
                                    td { +if (line.startEl == null) "-" else line.startEl!!.toString() }
                                    td { +if (line.startAzCompass == null) "-" else line.startAzCompass!! }
                                    td { +if (line.maxUTC == null) "-" else "${oMaxDate.hours}:${oMaxDate.minutes}" }
                                    td { +if (line.maxEl == null) "-" else line.maxEl!!.toString() }
                                    td { +if (line.maxAzCompass == null) "-" else line.maxAzCompass!! }
                                    td { +if (line.endUTC == null) "-" else "${oEndDate.hours}:${oEndDate.minutes}" }
                                    td { +if (line.endEl == null) "-" else line.endEl!!.toString() }
                                    td { +if (line.endAzCompass == null) "-" else line.endAzCompass!! }
                                }
                            }
                        }
                    }





                } else {
                    +"No visual passes"
                }
                form {
                    action = application.href(Satellites.NoradId(noradId = pass.info?.satid.toString()))

                    button(classes = "btn btn-primary mb-1") {
                        type = ButtonType.submit
                        +"""Sat detail"""
                    }
                }
            }
        }
    }
}

