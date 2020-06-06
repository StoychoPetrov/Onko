package eu.mobile.onko.activities.doctorActivities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import eu.mobile.onko.adapters.ReservationsAdapter
import eu.mobile.onko.communicationClasses.PostRequest
import eu.mobile.onko.communicationClasses.ResponseListener
import eu.mobile.onko.databinding.FragmentDoctorReservationsBinding
import eu.mobile.onko.globalClasses.GlobalData
import eu.mobile.onko.globalClasses.Utils
import eu.mobile.onko.models.ReservationModel
import kotlinx.android.synthetic.main.activity_reservations.*
import kotlinx.android.synthetic.main.fragment_doctor_reservations.*
import org.json.JSONException
import org.json.JSONObject

class DoctorReservationsFragment : Fragment(), ResponseListener, ReservationsAdapter.OnItemCLicked {

    private lateinit var binding: FragmentDoctorReservationsBinding

    private lateinit var gson: Gson

    private lateinit var reservationsList: List<ReservationModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding                                           = FragmentDoctorReservationsBinding.inflate(inflater, container, false)
        binding.doctorReservationsRecyclerView.adapter    = ReservationsAdapter(true, this)

        binding.swipeRefreshLayout.setOnRefreshListener {
            getReservations()
        }

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat(Utils.SERVER_DATE_TIME)
        gsonBuilder.setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gson = gsonBuilder.create()

        getReservations()

        return binding.root
    }

    private fun getReservations() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put(Utils.USER_ID        , GlobalData.getInstance().getmUserId())
            jsonObject.put(Utils.IS_COMPLETED   , (arguments?.getInt("position") ?: 0) == 1)
            val postRequest = PostRequest(requireContext(), jsonObject, this)
            postRequest.setmHttpMethod("POST")
            postRequest.execute(Utils.URL.plus(Utils.RESERVATIONS).plus(Utils.GET_RESERVATIONS_BY_DOCTOR))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun updateReservation(reservationModel: ReservationModel) {
        try {

            val body = gson.toJson(reservationModel)

            val postRequest = PostRequest(requireContext(), JSONObject(body), this)
            postRequest.setmHttpMethod("POST")
            postRequest.execute(Utils.URL.plus(Utils.RESERVATIONS).plus(reservationModel.id))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onResponseReceived(url: String?, responseCode: Int, response: String?) {
        if(url == Utils.URL.plus(Utils.RESERVATIONS).plus(Utils.GET_RESERVATIONS_BY_DOCTOR)) {
            if(responseCode == Utils.STATUS_SUCCESS) {
                binding.swipeRefreshLayout.isRefreshing = false
                reservationsList = gson.fromJson(response, Array<ReservationModel>::class.java).toList()

                requireActivity().runOnUiThread {
                    (doctorReservationsRecyclerView.adapter as ReservationsAdapter).update(reservationsList)
                }
            }
        }
        else {
            getReservations()
        }
    }

    override fun onConfirmClicked(position: Int) {
        val item = reservationsList[position]
        item.reservationStatus   = 3
        updateReservation(item)
    }

    override fun onRejectClicked(position: Int) {
        val item = reservationsList[position]
        item.reservationStatus   = 4
        updateReservation(item)
    }
}