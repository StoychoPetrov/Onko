package eu.mobile.onko.globalClasses

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import eu.mobile.onko.R
import eu.mobile.onko.globalClasses.Utils.DATE_FORMAT
import eu.mobile.onko.globalClasses.Utils.TIME_FORMAT
import eu.mobile.onko.models.CreateReservationModel
import kotlinx.android.synthetic.main.dialog_reservation_confirmation.*
import java.text.SimpleDateFormat
import java.util.*

class ReservationDialog(
        context: Context,
        private val createReservationModel: CreateReservationModel,
        private val listener: OnButtonClickListeners
) : Dialog(context), View.OnClickListener {

    interface OnButtonClickListeners {
        fun onPositiveClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_reservation_confirmation)

        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        setListeners()

        setData()

        negativeBtn.visibility  = View.VISIBLE
    }

    private fun setData() {

        val calendar                = Calendar.getInstance()
        calendar.time                         = createReservationModel.date

        hourTxt.text                = SimpleDateFormat("EEEE", Locale("bg")).format(calendar.time).plus("\n").plus(Utils.formatDate(createReservationModel.date, TIME_FORMAT).plus("ч."))
        reservationDateTxt.text     = Utils.formatDate(createReservationModel.date, DATE_FORMAT)
        doctorTxt.text              = createReservationModel.doctorTitle
        customerNameTxt.text        = createReservationModel.customerName
        doctorNameTxt.text          = createReservationModel.doctorName
    }

    private fun setListeners() {
        negativeBtn.setOnClickListener(this)
        positiveBtn.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        dismiss()

        when(view?.id) {
            positiveBtn.id -> listener.onPositiveClicked()
        }
    }
}