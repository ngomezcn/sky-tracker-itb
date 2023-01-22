package com.example.html.account

import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.html.*
import com.example.routes.Account
import com.example.routes.Satellites
import com.example.orm.tables.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import jdk.jshell.spi.ExecutionControl
import kotlinx.html.*

fun FlowContent.trackingList(application: Application) {
    section("py-5") {
        div("container px-5 my-5") {
            div("text-center mb-5") {
                h1("fw-bolder") { +"""My Tracking List""" }
            }
            //+"""<!-- FAQ Accordion 1-->"""
            div("accordion mb-5") {
                id = "accordionExample"
                div("accordion-item") {
                    h3("accordion-header") {
                        id = "headingOne"
                        button(classes = "accordion-button") {
                            type = ButtonType.button
                            attributes["data-bs-toggle"] = "collapse"
                            attributes["data-bs-target"] = "#collapseOne"
                            attributes["aria-expanded"] = "true"
                            attributes["aria-controls"] = "collapseOne"
                            +"""OBJECT B"""
                        }
                    }
                    div("row no-gutters") {
                        div("accordion-collapse collapse show") {
                            id = "collapseOne"
                            attributes["aria-labelledby"] = "headingOne"
                            attributes["data-bs-parent"] = "#accordionExample"
                            div("accordion-body") {
                                +"""Haga click en la fecha para descargar una carta estelar del paso."""
                                br {
                                }
                                br {
                                }

                                table("table table-bordered table-striped thead-dark") {
                                    thead {
                                        tr {
                                            th {
                                                rowSpan = "2"
                                                +"""Fecha"""
                                            }
                                            th {
                                                colSpan = "3"
                                                +"""Inicio"""
                                            }
                                            th {
                                                colSpan = "3"
                                                +"""Punto más alto"""
                                            }
                                            th {
                                                colSpan = "3"
                                                +"""Fin"""
                                            }
                                            th {
                                                rowSpan = "2"
                                                +"""Tipo de paso"""
                                            }
                                        }
                                        tr {
                                            td { +"""Hora""" }
                                            td { +"""Alt.""" }
                                            td { +"""Ac.""" }
                                            td { +"""Hora""" }
                                            td { +"""Alt.""" }
                                            td { +"""Ac.""" }
                                            td { +"""Hora""" }
                                            td { +"""Alt.""" }
                                            td { +"""Ac.""" }
                                        }
                                    }
                                    tbody {
                                        tr("clickableRow") {

                                            td {
                                                a {
                                                    href =
                                                        "passdetails.aspx?lat=0&amp;lng=0&amp;loc=Unspecified&amp;alt=0&amp;tz=UCT&amp;satid=25544&amp;mjd=59975.4224269135&amp;type=A"
                                                    title = "Mostrar la trayectoria terrestre durante este paso"
                                                    +"""31
                                                        ene"""
                                                }
                                            }
                                            td { +"""10:05:03""" }
                                            td { +"""10°""" }
                                            td { +"""SSO""" }
                                            td { +"""10:08:17""" }
                                            td { +"""52°""" }
                                            td { +"""SE""" }
                                            td { +"""10:11:32""" }
                                            td { +"""10°""" }
                                            td { +"""NE""" }
                                            td { +"""luz día""" }
                                        }
                                        tr("clickableRow") {

                                            td {
                                                a {
                                                    href =
                                                        "passdetails.aspx?lat=0&amp;lng=0&amp;loc=Unspecified&amp;alt=0&amp;tz=UCT&amp;satid=25544&amp;mjd=59975.9054907671&amp;type=A"
                                                    title = "Mostrar la trayectoria terrestre durante este paso"
                                                    +"""31
                                                        ene"""
                                                }
                                            }
                                            td { +"""21:40:52""" }
                                            td { +"""10°""" }
                                            td { +"""N""" }
                                            td { +"""21:43:54""" }
                                            td { +"""32°""" }
                                            td { +"""NE""" }
                                            td { +"""21:46:56""" }
                                            td { +"""10°""" }
                                            td { +"""ESE""" }
                                            td { +"""noche (sin luz)""" }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /*div("accordion-item") {
                        h3("accordion-header") {
                            id = "headingTwo"
                            button(classes = "accordion-button collapsed") {
                                type = "button"
                                attributes["data-bs-toggle"] = "collapse"
                                attributes["data-bs-target"] = "#collapseTwo"
                                attributes["aria-expanded"] = "false"
                                attributes["aria-controls"] = "collapseTwo"
                                +"""Accordion Item #2"""
                            }
                        }
                        div("accordion-collapse collapse") {
                            id = "collapseTwo"
                            attributes["aria-labelledby"] = "headingTwo"
                            attributes["data-bs-parent"] = "#accordionExample"
                            div("accordion-body") {
                                strong { +"""This is the second item's accordion body.""" }
                                +"""It is hidden by default, until the collapse plugin adds the appropriate classes that
                                    we
                                    use to style each element. These classes control the overall appearance, as well as
                                    the
                                    showing and hiding via CSS transitions. You can modify any of this with custom CSS
                                    or
                                    overriding our default variables. It's also worth noting that just about any HTML
                                    can go
                                    within the"""
                                code { +""".accordion-body""" }
                                +""", though the transition does limit overflow."""
                            }
                        }
                    }*/
                    /*div("accordion-item") {
                        h3("accordion-header") {
                            id = "headingThree"
                            button(classes = "accordion-button collapsed") {
                                type = "button"
                                attributes["data-bs-toggle"] = "collapse"
                                attributes["data-bs-target"] = "#collapseThree"
                                attributes["aria-expanded"] = "false"
                                attributes["aria-controls"] = "collapseThree"
                                +"""Accordion Item #3"""
                            }
                        }
                        div("accordion-collapse collapse") {
                            id = "collapseThree"
                            attributes["aria-labelledby"] = "headingThree"
                            attributes["data-bs-parent"] = "#accordionExample"
                            div("accordion-body") {
                                strong { +"""This is the third item's accordion body.""" }
                                +"""It is hidden by default, until the collapse plugin adds the appropriate classes that
                                    we
                                    use to style each element. These classes control the overall appearance, as well as
                                    the
                                    showing and hiding via CSS transitions. You can modify any of this with custom CSS
                                    or
                                    overriding our default variables. It's also worth noting that just about any HTML
                                    can go
                                    within the"""
                                code { +""".accordion-body""" }
                                +""", though the transition does limit overflow."""
                            }
                        }
                    }*/
                }
            }
        }
    }
}
