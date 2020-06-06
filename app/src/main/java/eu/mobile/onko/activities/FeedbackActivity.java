package eu.mobile.onko.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import eu.mobile.onko.R;
import eu.mobile.onko.globalClasses.GlobalData;
import eu.mobile.onko.globalClasses.Utils;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText    mFromEdt;
    private EditText    mToEdt;
    private EditText    mSubjectEdt;
    private Button      mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initUI();
        setListener();
    }

    private void initUI(){
        mFromEdt    = findViewById(R.id.from_edt);
        mToEdt      = findViewById(R.id.to_edt);
        mSubjectEdt = findViewById(R.id.subject_edt);
        mSendBtn    = findViewById(R.id.send_btn);

        mFromEdt.setText(GlobalData.getInstance().getmUserEmail());
        mToEdt.setText(Utils.SUPPORT_EMAIL);
        mToEdt.setEnabled(false);
    }

    private void setListener(){
        mSendBtn.setOnClickListener(this);
    }

    private void sendEmail(){

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSendBtn.getId())
            sendEmail();
    }
}
