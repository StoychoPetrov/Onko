package eu.mobile.onko.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.mobile.onko.R;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.Utils;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener{

    private TextInputEditText   mEmailEdt;
    private TextInputEditText   mPasswordEdt;
    private TextInputEditText   mRepeatPasswordEdt;
    private TextInputEditText   mFirstNameEdt;
    private TextInputEditText   mLastNameEdt;
    private TextInputEditText   mPhoneNumberEdt;
    private Button              mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initUI();
        setListeners();
    }

    private void initUI(){
        mEmailEdt           = (TextInputEditText) findViewById(R.id.email_edt);
        mPasswordEdt        = (TextInputEditText) findViewById(R.id.password_edt);
        mRepeatPasswordEdt  = (TextInputEditText) findViewById(R.id.repeat_password_edt);
        mFirstNameEdt       = (TextInputEditText) findViewById(R.id.first_name_edt);
        mLastNameEdt        = (TextInputEditText) findViewById(R.id.last_name_edt);
        mPhoneNumberEdt     = (TextInputEditText) findViewById(R.id.phone_edt);
        mSignUpBtn          = (Button)            findViewById(R.id.sign_up_btn);
    }

    private void setListeners(){
        mSignUpBtn.setOnClickListener(this);
    }

    private boolean validate(){
        if(mEmailEdt.getText().toString().isEmpty()){
            return false;
        }

        if(mPasswordEdt.getText().toString().isEmpty()){
            return false;
        }

        if(mRepeatPasswordEdt.getText().toString().isEmpty()){
            return false;
        }

        if(!mPasswordEdt.getText().toString().equals(mRepeatPasswordEdt.getText().toString())){
            return false;
        }

        if(mFirstNameEdt.getText().toString().isEmpty()){
            return false;
        }

        if(mLastNameEdt.getText().toString().isEmpty()){
            return false;
        }

        if(mPhoneNumberEdt.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }

    private void onSignUpClicked(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Utils.USER_MKB, new JSONArray());
            jsonObject.put(Utils.USER_EMAIL         , mEmailEdt.getText().toString());
            jsonObject.put(Utils.USER_PASSWORD      , mPasswordEdt.getText().toString());
            jsonObject.put(Utils.USER_FIRST_NAME    , mFirstNameEdt.getText().toString());
            jsonObject.put(Utils.USER_LAST_NAME     , mLastNameEdt.getText().toString());
            jsonObject.put(Utils.USER_PHONE         , mPhoneNumberEdt.getText().toString());
        }catch (JSONException exception){
            exception.printStackTrace();
        }

        PostRequest register = new PostRequest(this, jsonObject, this);
        register.execute(Utils.URL + Utils.REGISTER);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSignUpBtn.getId()){
            if(validate()){
                onSignUpClicked();
            }
        }
    }

    @Override
    public void onResponseReceived(int responseCode, String result) {
        if(responseCode == Utils.STATUS_SUCCESS){
            Toast.makeText(this, getString(R.string.registration_is_successfull), Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show();
        }
    }
}
