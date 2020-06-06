package eu.mobile.onko.models

import java.util.*

data class ReservationModel(

        var id: Int?                    = null,

        var doctorTitle: String?        = null,

        var date: Date?                 = null,

        var reservationStatus: Int?     = null,

        var doctorEmail: String?        = null,

        var doctorName: String?         = null,

        var doctorPhone: String?        = null,

        var clientEmail: String?        = null,

        var clientName: String?         = null,

        var clientPhone: String?        = null
)