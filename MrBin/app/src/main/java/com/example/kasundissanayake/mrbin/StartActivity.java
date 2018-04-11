package com.example.kasundissanayake.mrbin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button mregBtn;
    private Button mloginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mregBtn = (Button) findViewById(R.id.start_reg_btn);
        mloginBtn = (Button) findViewById(R.id.start_login_btn);

        mregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent = new Intent(StartActivity.this,VerificationActivity.class);
                startActivity(reg_intent);
            }
        });
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start_login_intent = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(start_login_intent);
            }
        });

    }
}
