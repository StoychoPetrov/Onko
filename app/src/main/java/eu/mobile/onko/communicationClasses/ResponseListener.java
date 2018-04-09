package eu.mobile.onko.communicationClasses;

import org.json.JSONObject;

/**
 * Created by Stoycho Petrov on 9.4.2018 г..
 */

public interface ResponseListener {

    void onResponseReceived(int responseCode, JSONObject jsonObject);
}
