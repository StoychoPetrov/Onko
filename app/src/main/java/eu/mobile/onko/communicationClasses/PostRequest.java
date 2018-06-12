package eu.mobile.onko.communicationClasses;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import eu.mobile.onko.R;

/**
 * Created by Stoycho Petrov on 8.4.2018 г..
 */

public class PostRequest extends AsyncTask<String, Void, String>{

    private Context             mContext;

    private JSONObject          mPostData;
    private ResponseListener    mListener;
    private int                 mResponseCode;
    private String              mHttpMethod     = "POST";
    private String              mUrl            = "";

    public PostRequest(Context context, JSONObject jsonObject, ResponseListener listner){
        mContext    = context;
        mPostData   = jsonObject;
        mListener   = listner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        showProgress(true);
    }

    @Override
    protected String doInBackground(String... urls) {

        mUrl = urls[0]; // URL to call

        OutputStream out = null;
        try {

            URL url = new URL(mUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(mHttpMethod);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if(mHttpMethod.equalsIgnoreCase("POST")) {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
            }
            urlConnection.connect();

            if(mHttpMethod.equalsIgnoreCase("POST")) {
                out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

                String data = mPostData.toString(); //data to post
                writer.write(data);
                Log.d("HTTP_SENT", data);

                writer.flush();

                writer.close();

                out.close();
            }

            int responseCode    = urlConnection.getResponseCode();

            Log.d("HTTP_RESPONSE_CODE:", String.valueOf(responseCode));


            StringBuilder builder = new StringBuilder();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    builder.append(line);
                }

                Log.d("HTTP_RESPONSE_MESSAGE:", builder.toString());
            }
            else {
                builder.append("");
            }

            mResponseCode = responseCode;
            return builder.toString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        mResponseCode = -1;
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        showProgress(false);
        mListener.onResponseReceived(mUrl, mResponseCode, result);
    }

    private void showProgress(final boolean visible){
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View background     = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.progress_background);
                View progressView   = ((Activity)mContext).getWindow().getDecorView().findViewById(R.id.progress_view);

                background.setVisibility(visible ? View.VISIBLE : View.GONE);
                progressView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    public String getmHttpMethod() {
        return mHttpMethod;
    }

    public void setmHttpMethod(String mHttpMethod) {
        this.mHttpMethod = mHttpMethod;
    }
}
