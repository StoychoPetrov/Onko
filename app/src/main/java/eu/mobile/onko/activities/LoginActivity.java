package eu.mobile.onko.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import eu.mobile.onko.R;
import eu.mobile.onko.activities.doctorActivities.DoctorsMainActivity;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.ConfirmFingerprintDialog;
import eu.mobile.onko.globalClasses.FingerprintHandler;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.KeystoreHandler;
import eu.mobile.onko.globalClasses.LinkFingerprintDialog;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.UserTypeEnum;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, FingerprintHandler.OnAuthenticationSucceededListener, FingerprintHandler.OnAuthenticationErrorListener{

    public final static int             USE_FINGERPRINT             = 90;

    private EditText                    mPasswordEdt;
    private EditText                    mEmailEdt;
    private Button                      mLoginBtn;
    private Button                      mSignUpBtn;

    private KeystoreHandler             mKeystoreHandler;
    private SharedPreferences           mSharedPreferences;
    private FingerprintHandler          mFingerprintHandler;
    private ConfirmFingerprintDialog    mConfirmFingerprintDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setListeners();
        setFingerPrintScanner();
    }

    private void initUI(){
        mEmailEdt       = (EditText)    findViewById(R.id.email_edt);
        mPasswordEdt    = (EditText)    findViewById(R.id.password_edt);
        mLoginBtn       = (Button)      findViewById(R.id.login_btn);
        mSignUpBtn      = (Button)      findViewById(R.id.sign_up_btn);

        mKeystoreHandler    = new KeystoreHandler();
        mSharedPreferences  = PreferenceManager.getDefaultSharedPreferences(this);
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

    private void goToDoctorActivity() {
        Intent intent = new Intent(this, DoctorsMainActivity.class);
        startActivity(intent);
        finish();
    }

    private  void login(String email, String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Utils.EMAIL      , email);
            jsonObject.put(Utils.PASSWORD   , password);
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

    private void showEnableFingerprintDialog(boolean isNewFingerprint){
        LinkFingerprintDialog linkFingerprintDialog = new LinkFingerprintDialog(this, isNewFingerprint);
        linkFingerprintDialog.setEnableFingerPrintListener(new LinkFingerprintDialog.EnableFingerprintListener() {
            @Override
            public void onError() {
                //login(mEmailEdt.getText().toString(), mPasswordEdt.getText().toString());
                if(GlobalData.getInstance().getmUserType() == UserTypeEnum.Patient.getId())
                    goToMainActivity();
                else
                    goToDoctorActivity();
            }

            @Override
            public void onAthSuccess() {

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Utils.PREFERENCES_USER_EMAIL                       , mEmailEdt.getText().toString());
                editor.putString(Utils.PREFERENCES_USER_PASSWORD                    , mPasswordEdt.getText().toString());
                editor.putBoolean(Utils.PREFERENCES_USE_FINGERPRINT_WHEN_LOG_IN     , true);
                editor.apply();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    if(GlobalData.getInstance().getmUserType() == UserTypeEnum.Patient.getId())
                        goToMainActivity();
                    else
                        goToDoctorActivity();
                    }
                }, 300);
            }
        });
        linkFingerprintDialog.showDialog();
    }

    private void setFingerPrintScanner(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, USE_FINGERPRINT);
                return;
            }
        }

        mFingerprintHandler = new FingerprintHandler(this);

        if(mSharedPreferences.getBoolean(Utils.PREFERENCES_USE_FINGERPRINT_WHEN_LOG_IN, true) &&
                !mSharedPreferences.getString(Utils.PREFERENCES_USER_PASSWORD, "").isEmpty() &&
                mFingerprintHandler.isFingerScannerAvailableAndSet()) {

            mFingerprintHandler.setOnAuthenticationSucceededListener(this);
            mFingerprintHandler.setOnAuthenticationFailedListener(this);

            if (mFingerprintHandler.isFingerScannerAvailableAndSet()) {
                if (mSharedPreferences.getBoolean(Utils.PREFERENCES_USE_FINGERPRINT_WHEN_LOG_IN, true)) {
                    mKeystoreHandler = new KeystoreHandler();
                    mKeystoreHandler.prepareKey();
                }
            }
        }
    }

    private void setUseFingerprintWhenLogIn(boolean useFingerprint){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Utils.PREFERENCES_USE_FINGERPRINT_WHEN_LOG_IN, useFingerprint);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void startFingerprintListening(){
        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(mKeystoreHandler.getCipher());
        mFingerprintHandler.startListening(cryptoObject);

        if(mConfirmFingerprintDialog == null) {
            mConfirmFingerprintDialog = new ConfirmFingerprintDialog(this);
            mConfirmFingerprintDialog.setConfirmFingerprintDialogListener(new ConfirmFingerprintDialog.ConfirmFingerprintDialogListener() {
                @Override
                public void onUseLoginCodePressed() {
                    mFingerprintHandler.stopListening();
                }

                @Override
                public void onBackPressed() {
                    mConfirmFingerprintDialog.dismiss();
                    finish();
                }
            });
            mConfirmFingerprintDialog.showDialog();
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == mLoginBtn.getId()){
            if(validate())
                login(mEmailEdt.getText().toString(), mPasswordEdt.getText().toString());
        }
        else if(view.getId() == mSignUpBtn.getId()){
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String result) {
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
                GlobalData.getInstance().setmUserType(jsonObject.getInt(Utils.USER_TYPE_ID));
            }catch (JSONException exception){
                exception.printStackTrace();
            }

            boolean isFingerPrintSet = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getBoolean(Utils.PREFERENCES_USE_FINGERPRINT_WHEN_LOG_IN, false);

            if((mFingerprintHandler.isFingerScannerAvailableAndSet() && !isFingerPrintSet)) {
                showEnableFingerprintDialog(false);
            }
            else if(GlobalData.getInstance().getmUserType() == UserTypeEnum.Patient.getId())
                goToMainActivity();
            else
                goToDoctorActivity();
        }
        else if(responseCode == Utils.STATUS_NOT_FOUND){
            Toast.makeText(LoginActivity.this, getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(LoginActivity.this, getString(R.string.connection_problem), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mSharedPreferences.getBoolean(Utils.PREFERENCES_USE_FINGERPRINT_WHEN_LOG_IN, true) &&
                !mSharedPreferences.getString(Utils.PREFERENCES_USER_PASSWORD, "").isEmpty() &&
                mFingerprintHandler.isFingerScannerAvailableAndSet()) {
            try {
                if (mKeystoreHandler.initCipher(Utils.FINGERPRINT_KEY_NAME)) {
                    startFingerprintListening();
                }
            } catch (Exception e){
                try {
                    boolean canUseFingerprint = mKeystoreHandler.createFingerPrintKeyForOldClient();
                    setUseFingerprintWhenLogIn(canUseFingerprint);
                    onResume();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAuthSucceeded() {
        mFingerprintHandler.stopListening();
        mConfirmFingerprintDialog.setFingerprintCorrect();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login(mSharedPreferences.getString(Utils.PREFERENCES_USER_EMAIL, ""), mSharedPreferences.getString(Utils.PREFERENCES_USER_PASSWORD, ""));
                mConfirmFingerprintDialog.dismiss();
            }
        }, 200);
    }

    @Override
    public void onAuthFailed() {
        mConfirmFingerprintDialog.setFingerprintIncorrect();
    }
}
