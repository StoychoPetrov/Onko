package eu.mobile.onko.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import eu.mobile.onko.R
import eu.mobile.onko.databinding.ItemDoctorReservationBinding
import eu.mobile.onko.databinding.ItemReservationBinding
import eu.mobile.onko.models.ReservationModel
import java.util.*


class ReservationsAdapter(private val isForDoctor: Boolean, private val listener: OnItemCLicked?) : RecyclerView.Adapter<ReservationsAdapter.ViewHolder>(){

    private val reservations         = mutableListOf<ReservationModel>()

    interface OnItemCLicked {

        fun onConfirmClicked(position: Int)

        fun onRejectClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(parent.context).inflate(
            if(isForDoctor) R.layout.item_doctor_reservation else R.layout.item_reservation, parent, false))

    override fun getItemCount(): Int
            = reservations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bindTo(reservations[position])

    fun update(list: List<ReservationModel>) {
        reservations.clear()
        reservations.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = if(!isForDoctor) ItemReservationBinding.bind(itemView) else ItemDoctorReservationBinding.bind(itemView)

        fun bindTo(reservation: ReservationModel) {

            val date = Calendar.getInstance()
            val previous = Calendar.getInstance()

            if(isForDoctor)
                (binding as ItemDoctorReservationBinding).reservation = reservation
            else
                (binding as ItemReservationBinding).reservation = reservation

            if (adapterPosition != 0 && reservation.date != null && reservations[adapterPosition - 1].date != null) {
                date.time       = reservation.date
                previous.time   = reservations[adapterPosition - 1].date

                if(isForDoctor)
                    (binding as ItemDoctorReservationBinding).dateVisible = (date.get(Calendar.DAY_OF_MONTH) != previous.get(Calendar.DAY_OF_MONTH)
                            || date.get(Calendar.MONTH) != previous.get(Calendar.MONTH)
                            || date.get(Calendar.YEAR) != previous.get(Calendar.YEAR))
                else
                    (binding as ItemReservationBinding).dateVisible = (date.get(Calendar.DAY_OF_MONTH) != previous.get(Calendar.DAY_OF_MONTH)
                            || date.get(Calendar.MONTH) != previous.get(Calendar.MONTH)
                            || date.get(Calendar.YEAR) != previous.get(Calendar.YEAR))
            }
            else if(adapterPosition == 0) {
                if(isForDoctor)
                    (binding as ItemDoctorReservationBinding).dateVisible = true
                else
                    (binding as ItemReservationBinding).dateVisible = true
            }

            if(isForDoctor && (reservation.reservationStatus == 2 || reservation.reservationStatus == 3)) {
                (binding as ItemDoctorReservationBinding).settingsImg.visibility    = View.VISIBLE
                binding.settingsImg.setOnClickListener {
                    showSettingsPopUp()
                }
            }
            else if(isForDoctor)
                (binding as ItemDoctorReservationBinding).settingsImg.visibility    = View.GONE
        }

        private fun showSettingsPopUp() {
            val popup = PopupMenu(itemView.context, (binding as ItemDoctorReservationBinding).settingsImg)
            //Inflating the Popup using xml file
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.reservation_menu, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener { item ->

                if(item.itemId == R.id.reject)
                    listener?.onRejectClicked(adapterPosition)
                else
                    listener?.onConfirmClicked(adapterPosition)

                true
            }

            popup.show() //showing popup menu
        }
    }
}