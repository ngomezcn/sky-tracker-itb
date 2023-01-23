package com.example.html.account.trackingList

import com.example.*
import com.example.html.account.trackingList.itemList
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
            div("accordion mb-5") {
                id = "accordionExample"
                div("accordion-item") {

                    for (pass in visualPasses) {
                        itemList(application, pass)

                    }
                }
            }
        }
    }
}

