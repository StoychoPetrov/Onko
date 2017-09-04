package eu.mobile.onko;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText    mPasswordEdt;
    private EditText    mEmailEdt;
    private Button      mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    private void initUI(){
        mEmailEdt       = (EditText)    findViewById(R.id.email_edt);
        mPasswordEdt    = (EditText)    findViewById(R.id.password_edt);
        mLoginBtn       = (Button)      findViewById(R.id.login_btn);
    }
}
