package com.example.templates.content

import com.example.epochToDate
import com.example.html.satellite.satellitesList.earthViewer
import com.example.html.satellite.satellitesList.ulSatelliteList
import com.example.models.n2yo.N2VisualPasses
import com.example.orm.tables.SatelliteDAO
import com.example.routes.Account
import com.example.routes.Satellites
import io.ktor.server.application.*
import io.ktor.server.resources.*
import kotlinx.html.*
import java.text.DateFormatSymbols

fun FlowContent.satellitesList(application: Application, sats: List<SatelliteDAO>, page: Int) {

            div("overlay") {
            id = "loading"
            div {
                div("text-center ") {
                    div("spinner-border") {
                        style = "width: 3rem; height: 3rem; color: #0D6EFD"
                        role = "status"
                    }
                }
            }
        }

        div("row no-gutters") {

            div("col-12 col-sm-6 col-md-8") {

                earthViewer()
            }

            div("col-6 col-md-4 mt-1") {
                id = "satListPanel"
                style="opacity: 0;"
                div("panel-heading") {
                    id = "sat_list_heading"
                    h3("panel-title") { +"""Artificial sats orbiting the earth""" }
                    h6("panel-title") { +"""Filters""" }
                }
                form {
                    div("row") {
                        div("col") {
                            div("form-check") {
                                input(classes = "form-check-input") {
                                    type = "radio"
                                    name = "exampleRadios"
                                    id = "exampleRadios1"
                                    value = "option1"
                                }
                                label("form-check-label") {
                                    htmlFor = "exampleRadios1"
                                    +"""No filter"""
                                }
                            }
                        }
                        div("col") {
                            div("form-check") {
                                input(classes = "form-check-input") {
                                    type = "radio"
                                    name = "exampleRadios"
                                    id = "exampleRadios1"
                                    value = "option1"
                                }
                                label("form-check-label") {
                                    htmlFor = "exampleRadios1"
                                    +"""Debris"""
                                }
                            }
                        }
                    }
                    div("row") {
                        div("col") {
                            div("form-check") {
                                input(classes = "form-check-input") {
                                    type = "radio"
                                    name = "exampleRadios"
                                    id = "exampleRadios1"
                                    value = "option1"
                                }
                                label("form-check-label") {
                                    htmlFor = "exampleRadios1"
                                    +"""Starklink"""
                                }
                            }
                        }
                        div("col") {
                            div("form-check") {
                                input(classes = "form-check-input") {
                                    type = "radio"
                                    name = "exampleRadios"
                                    id = "exampleRadios1"
                                    value = "option1"
                                    disabled = "true"
                                }
                                label("form-check-label") {
                                    htmlFor = "exampleRadios1"
                                    +"""Space stations (coming soon..)"""
                                }
                            }
                        }
                    }
                }

                nav {
                    ul("pagination") {
                       if(page!!-1 > 0) {
                            li("page-item") {
                                a(classes = "page-link") {
                                    href = application.href(Satellites(page = page - 1))
                                    tabIndex = "-1"
                                    +"""Previous"""
                                }
                            }
                            li("page-item") {
                                a(classes = "page-link") {
                                    href = application.href(Satellites(page = page - 1))
                                    +"${page!!-1}"
                                }
                            }
                        } else
                        {
                            li("page-item disabled") {
                                a(classes = "page-link") {
                                    href = application.href(Satellites(page = page - 1))
                                    tabIndex = "-1"
                                    +"""Previous"""
                                }
                            }
                        }
                        li("page-item active") {
                            a(classes = "page-link") {
                                href = "#"
                                +"${page!!}"
                            }
                        }
                        li("page-item") {
                            a(classes = "page-link") {
                                href = application.href(Satellites(page = page + 1))
                                +"${page!!+1}"
                            }
                        }
                        if(page!!-1 <= 0) {
                            li("page-item") {
                                a(classes = "page-link") {
                                    href = application.href(Satellites(page = page + 1))
                                    +"${page!!+2}"
                                }
                            }
                        }
                        li("page-item") {
                            a(classes = "page-link") {
                                href = application.href(Satellites(page = page + 1))
                                +"""Next"""
                            }
                        }
                    }
                }
               ulSatelliteList(application, sats)
            }
        }

        script {
            unsafe {
                +"const list = document.getElementById(\"sat_list\"); list.style.maxHeight = (window.innerHeight-72-66)+'px';"
            }
        }
}
