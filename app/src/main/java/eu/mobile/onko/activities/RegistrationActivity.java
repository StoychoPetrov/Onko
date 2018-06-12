package eu.mobile.onko.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Patterns;
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

        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals("")){ // for backspace
                    return source;
                }
                if(source.toString().matches("[a-zA-Z ]+")){
                    return source;
                }
                return "";
            }
        };

        mFirstNameEdt.setFilters(new InputFilter[]{inputFilter});
        mLastNameEdt.setFilters(new InputFilter[]{inputFilter});
    }

    private void setListeners(){
        mSignUpBtn.setOnClickListener(this);
    }

    private boolean validate(){
        if(mEmailEdt.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.please_enter_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mEmailEdt.getText().toString()).matches()){
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mPasswordEdt.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mRepeatPasswordEdt.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.please_repeat_password, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!mPasswordEdt.getText().toString().equals(mRepeatPasswordEdt.getText().toString())){
            Toast.makeText(this, R.string.passwords_are_not_equals, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mFirstNameEdt.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.please_enter_first_name, Toast.LENGTH_SHORT).show();
            return false;
        }


        if(mLastNameEdt.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.please_enter_last_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mPhoneNumberEdt.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.please_enter_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Patterns.PHONE.matcher(mPhoneNumberEdt.getText().toString()).matches()){
            Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void onSignUpClicked(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Utils.USER_MKB           , new JSONArray());
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
    public void onResponseReceived(String url, int responseCode, String result) {
        if(responseCode == Utils.STATUS_SUCCESS){
            Toast.makeText(this, getString(R.string.registration_is_successfull), Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show();
        }
    }
}
