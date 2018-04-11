package com.example.kasundissanayake.mrbin;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NumberActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private TextInputLayout mStatus;
    private Button mSaveBtn;
    private DatabaseReference mDatabseRef;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();

        mDatabseRef = FirebaseDatabase.getInstance().getReference().child("App_Users").child(currentUid);
        String status_value = getIntent().getStringExtra("status_value");

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mSaveBtn = (Button) findViewById(R.id.status_change_btn);
        mToolBar = (Toolbar) findViewById(R.id.status_tool_bar);

        mStatus.getEditText().setText(status_value);


        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress =new ProgressDialog((NumberActivity.this));
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Pease wait while we save the changes");
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();
                mDatabseRef.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Changes Successfully Done",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"There was some error on saving Changes",Toast.LENGTH_LONG).show();
                        }
                    }
                });




            }
        });


    }
}
