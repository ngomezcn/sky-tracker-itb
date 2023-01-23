package com.example.html.account

import com.example.*
import com.example.models.n2yo.N2VisualPasses
import io.ktor.server.application.*
import kotlinx.html.*
import java.text.DateFormatSymbols

fun FlowContent.trackingList(application: Application, visualPasses: List<N2VisualPasses>) {
    section("py-5") {
        div("container px-5 my-5") {
            div("text-center mb-5") {
                h1("fw-bolder") { +"""My Tracking List""" }
            }
            +"""<!-- FAQ Accordion 1-->"""
            div("accordion mb-5") {
                id = "accordionExample"
                div("accordion-item") {

                    for (pass in visualPasses) {
                        div("accordion-item") {
                            h3("accordion-header") {
                                id = "headingThree"
                                button(classes = "accordion-button collapsed") {
                                    type = ButtonType.button
                                    attributes["data-bs-toggle"] = "collapse"
                                    attributes["data-bs-target"] = "#collapse${pass.hashCode()}"
                                    attributes["aria-expanded"] = "false"
                                    attributes["aria-controls"] = "collapse${pass.hashCode()}"
                                    + if(pass.info?.satname == null) "unnamed" else pass.info!!.satname!!
                                }
                            }
                            div("accordion-collapse collapse") {
                                id = "collapse${pass.hashCode()}"
                                attributes["aria-labelledby"] = "headingThree"
                                attributes["data-bs-parent"] = "#accordionExample"
                                div("accordion-body") {

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
                                                /*th {
                                                    rowSpan = "2"
                                                    +"""Resources"""
                                                }*/
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
/*
                                            @SerialName("startAz"         ) var startAz         : Double? = null,
                                            @SerialName("startAzCompass"  ) var startAzCompass  : String? = null,
                                            @SerialName("startEl"         ) var startEl         : Double? = null,
                                            @SerialName("startUTC"        ) var startUTC        : Int?    = null,
                                            @SerialName("maxAz"           ) var maxAz           : Double? = null,
                                            @SerialName("maxAzCompass"    ) var maxAzCompass    : String? = null,
                                            @SerialName("maxEl"           ) var maxEl           : Double? = null,
                                            @SerialName("maxUTC"          ) var maxUTC          : Int?    = null,
                                            @SerialName("endAz"           ) var endAz           : Double? = null,
                                            @SerialName("endAzCompass"    ) var endAzCompass    : String? = null,
                                            @SerialName("endEl"           ) var endEl           : Int?    = null,
                                            @SerialName("endUTC"          ) var endUTC          : Int?    = null,
                                            @SerialName("mag"             ) var mag             : Double? = null,
                                            @SerialName("duration"        ) var duration        : Int?    = null,
                                            @SerialName("startVisibility" ) var startVisibility : Int?    = null?*/

                                            for(line in pass.passes)
                                            {

                                                val oStartDate = epochToDate(line.startUTC!!.toLong())
                                                val oMaxDate = epochToDate(line.maxUTC!!.toLong())
                                                val oEndDate = epochToDate(line.endUTC!!.toLong())

                                                tr("clickableRow") {

                                                    /*td {
                                                        a {
                                                            href =
                                                                "passdetails.aspx?lat=0&amp;lng=0&amp;loc=Unspecified&amp;alt=0&amp;tz=UCT&amp;satid=25544&amp;mjd=59975.4224269135&amp;type=A"
                                                            title = "Mostrar la trayectoria terrestre durante este paso"
                                                            +"""31
                                                        ene"""
                                                        }
                                                    }*/
                                                    // startUTC
                                                    // startEl
                                                    // startAzCompass

                                                    td { + "${ if(line.startUTC == null) "-" else "${oStartDate.day} of ${DateFormatSymbols().getMonths()[oStartDate.month]}"  }" }

                                                    td { + "${ if(line.startUTC == null) "-" else "${oStartDate.hours}:${oStartDate.minutes}"  }" }
                                                    td { + if(line.startEl == null) "-" else line.startEl!!.toString() }
                                                    td { + if(line.startAzCompass == null) "-" else line.startAzCompass!! }

                                                    td { + "${ if(line.maxUTC == null) "-" else "${oMaxDate.hours}:${oMaxDate.minutes}"  }" }
                                                    td { + if(line.maxEl == null) "-" else line.maxEl!!.toString() }
                                                    td { + if(line.maxAzCompass == null) "-" else line.maxAzCompass!! }


                                                    td { + "${ if(line.endUTC == null) "-" else "${oEndDate.hours}:${oEndDate.minutes}"  }" }
                                                    td { + if(line.endEl == null) "-" else line.endEl!!.toString() }
                                                    td { + if(line.endAzCompass == null) "-" else line.endAzCompass!! }

                                                    /*td {
                                                        unsafe {
                                                        +"<a href=\"https://heavens-above.com/PassSkyChart2.ashx?passID=4364&size=800&lat=0&lng=0&loc=Unspecified&alt=0&tz=UCT&showUnlit=true\" download>Obtain trajectory</a>\n"
                                                        }
                                                    }*/
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

