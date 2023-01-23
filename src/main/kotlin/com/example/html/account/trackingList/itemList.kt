package com.example.html.account.trackingList

import com.example.epochToDate
import com.example.models.n2yo.N2VisualPasses
import com.example.routes.Account
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
                                val oStartDate = epochToDate(line.startUTC!!.toLong())
                                val oMaxDate = epochToDate(line.maxUTC!!.toLong())
                                val oEndDate = epochToDate(line.endUTC!!.toLong())

                                tr("clickableRow") {

                                    td { +"${if (line.startUTC == null) "-" else "${oStartDate.day} of ${DateFormatSymbols().getMonths()[oStartDate.month]}"}" }
                                    td { +"${if (line.startUTC == null) "-" else "${oStartDate.hours}:${oStartDate.minutes}"}" }
                                    td { +if (line.startEl == null) "-" else line.startEl!!.toString() }
                                    td { +if (line.startAzCompass == null) "-" else line.startAzCompass!! }
                                    td { +"${if (line.maxUTC == null) "-" else "${oMaxDate.hours}:${oMaxDate.minutes}"}" }
                                    td { +if (line.maxEl == null) "-" else line.maxEl!!.toString() }
                                    td { +if (line.maxAzCompass == null) "-" else line.maxAzCompass!! }
                                    td { +"${if (line.endUTC == null) "-" else "${oEndDate.hours}:${oEndDate.minutes}"}" }
                                    td { +if (line.endEl == null) "-" else line.endEl!!.toString() }
                                    td { +if (line.endAzCompass == null) "-" else line.endAzCompass!! }
                                }
                            }
                        }
                    }

                    form {
                        action = application.href(Account.UntrackSat())
                        method = FormMethod.post
                        encType = FormEncType.multipartFormData

                        input {
                            type = InputType.hidden
                            name = "idSatellite"
                            value = "${pass.info!!.satid}"
                        }

                        button(classes = "btn btn-secondary mb-1") {
                            type = ButtonType.submit
                            +"""Untrack"""
                        }
                    }

                } else {
                    +"No visual passes"
                }
            }
        }
    }
}

