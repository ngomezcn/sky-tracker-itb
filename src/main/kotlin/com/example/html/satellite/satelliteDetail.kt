package com.example.html.satellite

import com.example.routes.Account
import com.example.routes.Satellites
import com.example.orm.tables.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import jdk.jshell.spi.ExecutionControl
import kotlinx.html.*

fun FlowContent.satelliteDetail(
    sat: SatelliteDAO,
    comments: List<SatCommentDAO>,
    isTracking: Boolean,
    application: Application
) {

    section("py-5") {
        div("container px-5 my-5") {
            div("row gx-5") {
                div("col-lg-9") {
                    //+"""-- Post content-->"""
                    article {
                        //+"""-- Post header-->"""
                        header("mb-4") {
                            //+"""-- Post title-->"""
                            h1("fw-bolder mb-1") {

                                +"${sat.objectName!!}  "
                                if(isTracking) {
                                    i("bg-dangerbi bi-star-fill") {
                                        style = "color: #FFD700;"
                                    }
                                }
                            }
                            //+"""-- Post meta content-->"""
                            div("text-muted fst-italic mb-2") { +("#" + sat.noradCatId!!) }
                            //+"""-- Post categories-->"""
                            a(classes = "badge bg-secondary text-decoration-none link-light") {
                                href = "#!"
                                +"""Debris"""
                            }
                            a(classes = "badge bg-secondary text-decoration-none link-light") {
                                href = "#!"
                                +"""Low orbit"""
                            }
                        }
                        //+"""-- Preview image figure-->"""
                        figure("mb-4") {
                            img(classes = "img-fluid rounded") {
                                src =
                                    "https://www.heavens-above.com/orbitdisplay.aspx?icon=iss&width=900&height=450&mode=M&satid=${sat.noradCatId}"
                                alt = "..."
                            }
                            p("text-center") { +"""Trayectoria de superficie""" }
                        }
                        div("container") {
                            div("row") {
                                div("col-lg-6 mb-4") {
                                    figure("mb-4") {
                                        img(classes = "img-fluid rounded") {
                                            src =
                                                "https://www.heavens-above.com/orbitdisplay.aspx?icon=iss&width=300&height=300&mode=N&satid=${sat.noradCatId}"
                                            alt = "..."
                                        }
                                        p("col-xs-10") { +"""Vista desde encima del plano orbital""" }
                                    }
                                }
                                div("col-lg-6 mb-4") {
                                    figure("mb-4") {
                                        img(classes = "img-fluid rounded") {
                                            src =
                                                "https://www.heavens-above.com/orbitdisplay.aspx?icon=iss&width=300&height=300&mode=A&satid=${sat.noradCatId}"
                                            alt = "..."
                                        }
                                        p("text-center") { +"""Vista desde encima del satélite""" }
                                    }
                                }
                            }
                        }
                        //+"""-- Post content-->"""
                        section("mb-5") {
                            p("font-weight-bold") {
                                +"""Los datos orbitales se extraen de los siguientes elementos
                                    orbitales de dos líneas:"""
                            }
                            table("table") {
                                tbody {
                                    tr {
                                        th {
                                            style = "width: 10%"
                                            scope = ThScope.row
                                            +"""TLE1"""
                                        }
                                        td { +sat.tleLine1!! }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""TLE2:"""
                                        }
                                        td { +sat.tleLine2!! }
                                    }
                                }
                            }
                            p("font-weight-bold") { +"""Información respecto al satélite:""" }
                            table("table") {
                                tbody {
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Norad id"""
                                        }
                                        td { + "${if(sat.noradCatId!! == null) "-" else sat.noradCatId}" }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Object Name"""
                                        }
                                        td { + "${if(sat.objectName!! == null) "-" else sat.objectName}" }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Object ID"""
                                        }
                                        td { + "${if(sat.objectID!! == null) "-" else sat.objectID}" }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Country code"""
                                        }
                                        td { + "${if(sat.countryCode!! == null) "-" else sat.countryCode}" }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Launch date"""
                                        }
                                        td { + "${if(sat.launchDate!! == null) "-" else sat.launchDate}" }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Launch site"""
                                        }
                                        td { + "${if(sat.site == null) "-" else sat.site}"  }
                                    }
                                    tr {
                                        th {
                                            scope = ThScope.row
                                            +"""Decay date"""
                                        }
                                        td { + "-" } //"${if(sat.decayDate!! == null) "-" else sat.decayDate}" }
                                    }
                                }
                            }
                        }
                    }
                    //+"""-- Comments section-->"""
                    section {
                        div("card bg-light") {
                            div("card-body") {
                                //+"""-- Comment form-->"""
                                form {
                                    action = application.href(Satellites.Comment())
                                    method = FormMethod.post
                                    encType = FormEncType.multipartFormData

                                    div("form-group") {
                                        textArea(classes = "form-control") {
                                            name = "message"
                                            rows = "5"
                                        }
                                    }

                                    div("form-row align-items-center") {

                                        div("col-auto my-1") {
                                            input(classes = "form-control") {
                                                placeholder = ""
                                                type = InputType.file
                                                name = "image"
                                            }
                                            input {
                                                type = InputType.hidden
                                                name = "idSatellite"
                                                value = "${sat.id}"
                                            }
                                        }

                                        div("col-auto my-1") {
                                            button(classes = "btn btn-primary") {
                                                type = ButtonType.submit
                                                +"""Submit"""
                                            }
                                        }
                                    }
                                }
                                br {
                                }
                                //+"""-- Comment with nested comments-->"""

                                for (comment in comments) {
                                    div("shadow-sm p-4 mb-4 bg-white") {
                                        div("d-flex") {
                                            div("flex-shrink-0") {
                                                img(classes = "rounded-circle") {
                                                    src = "https://dummyimage.com/50x50/ced4da/6c757d.jpg"
                                                    alt = "..."
                                                }
                                            }

                                            div("ms-3") {
                                                div("fw-bold") { +"${comment.username}" }
                                                p { +"${comment.comment}" }

                                                if (comment.imageName.isNotBlank()) {
                                                    img(classes = "img-responsive center-block img-fluid rounded") {
                                                        style = "height: 250px;"
                                                        src = "/assets/sats/" + comment.imageName
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                /*div("shadow-sm p-4 mb-4 bg-white") {
                                    div("d-flex") {
                                        div("flex-shrink-0") {
                                            img(classes = "rounded-circle") {
                                                src = "https://dummyimage.com/50x50/ced4da/6c757d.jpg"
                                                alt = "..."
                                            }
                                        }
                                        div("ms-3") {
                                            div("fw-bold") { +"""Commenter Name""" }
                                            +"""When I look at the universe and all the ways the universe wants to kill
                                                us,
                                                I find it hard to reconcile that with statements of beneficence."""
                                        }
                                    }
                                }
                                div("shadow-sm p-4 mb-4 bg-white") {
                                    div("d-flex") {
                                        div("flex-shrink-0") {
                                            img(classes = "rounded-circle") {
                                                src = "https://dummyimage.com/50x50/ced4da/6c757d.jpg"
                                                alt = "..."
                                            }
                                        }
                                        div("ms-3") {
                                            div("fw-bold") { +"""Commenter Name""" }
                                            p {
                                                +"""When I look at the universe and all the ways the universe wants to kill
                                                    us,
                                                    I find it hard to reconcile that with statements of beneficence."""
                                            }
                                            img(classes = "img-responsive center-block img-fluid rounded") {
                                                style = "height: 250px;"
                                                src =
                                                    "https://www.heavens-above.com/orbitdisplay.aspx?icon=iss&width=200&height=200&mode=A&satid=${sat.noradCatId}"
                                            }
                                        }
                                    }
                                }
                                div("shadow-sm p-4 mb-4 bg-white") {
                                    div("d-flex") {
                                        div("flex-shrink-0") {
                                            img(classes = "rounded-circle") {
                                                src = "https://dummyimage.com/50x50/ced4da/6c757d.jpg"
                                                alt = "..."
                                            }
                                        }
                                        div("ms-3") {
                                            div("fw-bold") { +"""Commenter Name""" }
                                            +"""When I look at the universe and all the ways the universe wants to kill
                                                us,
                                                I find it hard to reconcile that with statements of beneficence."""
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                }
            }
        }
    }

    if (isTracking) {

        form {
            action = application.href(Account.TrackingList())
            method = FormMethod.delete
            encType = FormEncType.multipartFormData

            input {
                type = InputType.hidden
                name = "idSatellite"
                value = "${sat.id}"
            }
            div("form-group") {
                button(classes = "btn btn-light mb-2") {
                    type = ButtonType.submit
                    +"""Already tracking"""
                }
            }
        }

    } else {
        form {
            action = application.href(Account.TrackingList())
            method = FormMethod.post
            encType = FormEncType.multipartFormData

            input {
                type = InputType.hidden
                name = "idSatellite"
                value = "${sat.id}"
            }
            div("form-group") {
                button(classes = "btn btn-primary mb-2") {
                    type = ButtonType.submit
                    +"""Track satellite!"""
                }
            }
        }
    }
}


