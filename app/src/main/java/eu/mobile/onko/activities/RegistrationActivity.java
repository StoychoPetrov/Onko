package eu.mobile.onko.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.mobile.onko.R;
import eu.mobile.onko.communicationClasses.PostRequest;
import eu.mobile.onko.communicationClasses.ResponseListener;
import eu.mobile.onko.globalClasses.PopupWindowHelper;
import eu.mobile.onko.globalClasses.Utils;
import eu.mobile.onko.models.DoctorModel;
import eu.mobile.onko.models.UserTypeEnum;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener, PopupWindowHelper.OnItemSelected {

    private final static int REQUEST_CODE_CHOOSE_DOCTOR = 1;

    private TextInputEditText   mEmailEdt;
    private TextInputEditText   mPasswordEdt;
    private TextInputEditText   mRepeatPasswordEdt;
    private TextInputEditText   mFirstNameEdt;
    private TextInputEditText   mLastNameEdt;
    private TextInputEditText   mPhoneNumberEdt;
    private TextInputEditText   mClientTypeEdt;
    private TextInputLayout     mClientTypeLayout;
    private TextInputEditText   mDoctorEdt;
    private Button              mSignUpBtn;

    private PopupWindowHelper   mPopupWindowHelper;

    private List<UserTypeEnum> mAccountTypes;
    private UserTypeEnum       mSelectedAccountType;

    private DoctorModel        mSelectedDoctorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initUI();

        mAccountTypes       = Arrays.asList(UserTypeEnum.values());
        ArrayList<String> titles = new ArrayList<>();
        for(int i = 0; i < mAccountTypes.size(); i++)
            titles.add(Utils.getAccountTypeName(this, i + 1));

        mPopupWindowHelper  = new PopupWindowHelper(this, titles);

        setListeners();
    }

    private void initUI(){
        mEmailEdt           = (TextInputEditText) findViewById(R.id.email_edt);
        mPasswordEdt        = (TextInputEditText) findViewById(R.id.password_edt);
        mRepeatPasswordEdt  = (TextInputEditText) findViewById(R.id.repeat_password_edt);
        mFirstNameEdt       = (TextInputEditText) findViewById(R.id.first_name_edt);
        mLastNameEdt        = (TextInputEditText) findViewById(R.id.last_name_edt);
        mPhoneNumberEdt     = (TextInputEditText) findViewById(R.id.phone_edt);
        mClientTypeEdt      = (TextInputEditText) findViewById(R.id.client_type_edt);
        mClientTypeLayout   = (TextInputLayout)   findViewById(R.id.client_type_layout);
        mSignUpBtn          = (Button)            findViewById(R.id.sign_up_btn);
        mDoctorEdt          =                     findViewById(R.id.doctor_edt);

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
        mClientTypeEdt.setOnClickListener(this);
        mPopupWindowHelper.setChosenListener(this);
        mDoctorEdt.setOnClickListener(this);
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
        else if(!Patterns.PHONE.matcher(mPhoneNumberEdt.getText().toString()).matches()){
            Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mClientTypeEdt.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.please_choose_client_type, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mDoctorEdt.getVisibility() == View.VISIBLE && mSelectedDoctorModel == null) {
            Toast.makeText(this, R.string.please_choose_doctor_type, Toast.LENGTH_SHORT).show();
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
            jsonObject.put(Utils.USER_TYPE_ID       , mSelectedAccountType.getId());

            if(mSelectedDoctorModel != null)
                jsonObject.put(Utils.USER_DOCTOR_ID     , mSelectedDoctorModel.getmDoctorId());
        }catch (JSONException exception){
            exception.printStackTrace();
        }

        PostRequest register = new PostRequest(this, jsonObject, this);
        register.execute(Utils.URL + Utils.REGISTER);
    }

    private void goToChooseDoctor() {
        Intent intent = new Intent(this, ChooseDoctorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_DOCTOR);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSignUpBtn.getId()){
            if(validate()){
                onSignUpClicked();
            }
        }
        else if(v.getId() == mClientTypeEdt.getId()) {
            mPopupWindowHelper.showPopup(mClientTypeEdt);
        }
        else if(v.getId() == mDoctorEdt.getId())
            goToChooseDoctor();
    }

    @Override
    public void onResponseReceived(String url, int responseCode, String result) {
        if(responseCode == Utils.STATUS_SUCCESS){
            if(url.equalsIgnoreCase(Utils.URL + Utils.REGISTER)) {
                Toast.makeText(this, getString(R.string.registration_is_successfull), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else {
            Toast.makeText(this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemChosen(String item, int position) {
        mSelectedAccountType    = mAccountTypes.get(position);
        mClientTypeEdt.setText(Utils.getAccountTypeName(this, mSelectedAccountType.getId()));
        findViewById(R.id.doctorLayout).setVisibility(mSelectedAccountType == UserTypeEnum.Doctor ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_CHOOSE_DOCTOR
                && resultCode == RESULT_OK) {
            DoctorModel doctorModel = data.getParcelableExtra(Utils.INTENT_DOCTOR);
            mDoctorEdt.setText(doctorModel.getmDoctorName());
            mSelectedDoctorModel    = doctorModel;
        }
    }
}
