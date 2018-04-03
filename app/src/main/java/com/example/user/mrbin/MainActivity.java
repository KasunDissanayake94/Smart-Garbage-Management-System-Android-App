package com.example.user.mrbin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    Button btnRegister,btnLogin;
    LinearLayout register,login;
    Animation uptodown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = (Button)findViewById(R.id.buttonregister);
        register=(LinearLayout)findViewById(R.id.register);
        login=(LinearLayout)findViewById(R.id.Login);
        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);

    }
}
