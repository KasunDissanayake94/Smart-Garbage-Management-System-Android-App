package com.example.kasundissanayake.mrbin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateAccnt;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    private Toolbar mToolBar;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set ToolBar
        mToolBar = (Toolbar) findViewById(R.id.register_toolbar);
        //Progress Bar
        mProgressBar = new ProgressDialog(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDisplayName = (TextInputLayout) findViewById(R.id.display_name_text);
        mEmail = (TextInputLayout) findViewById(R.id.reg_email_text);
        mPassword = (TextInputLayout)findViewById(R.id.reg_password_text);
        mCreateAccnt = (Button) findViewById(R.id.createBtn);

        mAuth = FirebaseAuth.getInstance();

        mCreateAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mProgressBar.setTitle("Registering User");
                    mProgressBar.setMessage("Please wait while we create your Account");
                    mProgressBar.setCanceledOnTouchOutside(false );
                    mProgressBar.show();
                    register_user(display,email,password);

                }



            }


        });



    }

    private void register_user(final String display, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uId = currentUser.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("App_Users").child(uId);

                    HashMap<String,String> userMap = new HashMap<String, String>();
                    userMap.put("name",display);
                    userMap.put("telephone","+94722345556");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mProgressBar.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });


                }else{
                    mProgressBar.hide();
                    Toast.makeText(RegisterActivity.this,"Cannot sign in.. Please check the form and try again..",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
