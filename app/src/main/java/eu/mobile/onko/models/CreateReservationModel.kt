package eu.mobile.onko.models

import java.util.*

data class CreateReservationModel(
        var date: Date?             = null,

        var doctorTitle: String?    = null,

        var doctorName: String?     = null,

        var customerName: String?   = null
)