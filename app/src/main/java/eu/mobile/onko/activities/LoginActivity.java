package eu.mobile.onko.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import eu.mobile.onko.R;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener{

    private EditText    mPasswordEdt;
    private EditText    mEmailEdt;
    private Button      mLoginBtn;
    private Button      mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setListeners();
    }

    private void initUI(){
        mEmailEdt       = (EditText)    findViewById(R.id.email_edt);
        mPasswordEdt    = (EditText)    findViewById(R.id.password_edt);
        mLoginBtn       = (Button)      findViewById(R.id.login_btn);
        mSignUpBtn      = (Button)      findViewById(R.id.sign_up_btn);
    }

    private void setListeners(){
        mLoginBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private  void login(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.EMAIL, mEmailEdt.getText().toString());
            jsonObject.put(Utils.PASSWORD, mPasswordEdt.getText().toString());
        }catch (JSONException exception){
            exception.printStackTrace();
        }

        PostRequest postRequest = new PostRequest(this, jsonObject, this);
        postRequest.execute(Utils.URL + Utils.LOGIN);
    }

    private boolean validate(){
        if(mEmailEdt.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.enter_your_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mPasswordEdt.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.enter_your_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mEmailEdt.getText().toString()).matches()) {
            Toast.makeText(LoginActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mLoginBtn.getId()){
            if(validate())
                login();
        }
        else if(view.getId() == mSignUpBtn.getId()){
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResponseReceived(int responseCode, String result) {
        if(responseCode == Utils.STATUS_SUCCESS){
            try {
                JSONObject jsonObject = new JSONObject(result);
                GlobalData.getInstance().setmUserId(jsonObject.getInt(Utils.ID));
                GlobalData.getInstance().setmUserEmail(jsonObject.getString(Utils.USER_EMAIL));
                GlobalData.getInstance().setmPassword(jsonObject.getString(Utils.USER_PASSWORD));
                GlobalData.getInstance().setmFirstName(jsonObject.getString(Utils.USER_FIRST_NAME));
                GlobalData.getInstance().setmLastName(jsonObject.getString(Utils.USER_LAST_NAME));
                GlobalData.getInstance().setmPhoneNumber(jsonObject.getString(Utils.USER_PHONE));
                GlobalData.getInstance().setmToken(jsonObject.getString(Utils.USER_TOKEN));
            }catch (JSONException exception){
                exception.printStackTrace();
            }
            goToMainActivity();
        }
        else if(responseCode == Utils.STATUS_NOT_FOUND){
            Toast.makeText(LoginActivity.this, getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(LoginActivity.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
    }
}
