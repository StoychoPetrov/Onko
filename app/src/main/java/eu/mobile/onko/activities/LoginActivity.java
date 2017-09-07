package eu.mobile.onko.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import eu.mobile.onko.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText    mPasswordEdt;
    private EditText    mEmailEdt;
    private Button      mLoginBtn;

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
    }

    private void setListeners(){
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == mLoginBtn.getId()){
            goToMainActivity();
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
