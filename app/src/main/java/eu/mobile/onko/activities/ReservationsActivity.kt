package eu.mobile.onko.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import eu.mobile.onko.R
import eu.mobile.onko.adapters.ReservationsAdapter
import eu.mobile.onko.communicationClasses.PostRequest
import eu.mobile.onko.communicationClasses.ResponseListener
import eu.mobile.onko.globalClasses.GlobalData
import eu.mobile.onko.globalClasses.Utils
import eu.mobile.onko.models.ReservationModel
import kotlinx.android.synthetic.main.activity_reservations.*
import kotlinx.android.synthetic.main.fragment_reservations.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject

class ReservationsActivity : AppCompatActivity(), View.OnClickListener, ResponseListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservations)

        toolbar.findViewById<TextView>(R.id.title_txt).text = getString(R.string.reservations)

        setListeners()
        setAdapter()
        getReservations()
    }

    private fun setListeners() {
        back_arrow_img.setOnClickListener(this)
    }

    private fun setAdapter() {
        findViewById<RecyclerView>(R.id.reservationsRecyclerView).adapter = ReservationsAdapter(false, null)
    }

    private fun getReservations() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put(Utils.USER_ID, GlobalData.getInstance().getmUserId())
            val postRequest = PostRequest(this, jsonObject, this)
            postRequest.setmHttpMethod("POST")
            postRequest.execute(Utils.URL.plus(Utils.RESERVATIONS).plus(Utils.GET_RESERVATIONS_BY_ID))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.back_arrow_img -> onBackPressed()
        }
    }

    override fun onResponseReceived(url: String?, responseCode: Int, response: String?) {
        if(url == Utils.URL.plus(Utils.RESERVATIONS).plus(Utils.GET_RESERVATIONS_BY_ID)) {
            if(responseCode == Utils.STATUS_SUCCESS) {

                val gsonBuilder = GsonBuilder()
                gsonBuilder.setDateFormat(Utils.SERVER_DATE_TIME)
                gsonBuilder.setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                val gson = gsonBuilder.create()

                val reservations = gson.fromJson(response, Array<ReservationModel>::class.java).toList()

                runOnUiThread {
                    (findViewById<RecyclerView>(R.id.reservationsRecyclerView).adapter as ReservationsAdapter).update(reservations)
                }
            }
        }
    }
}