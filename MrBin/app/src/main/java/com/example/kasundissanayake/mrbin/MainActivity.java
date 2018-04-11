package com.example.kasundissanayake.mrbin;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DatabaseReference mUserRef;

    private Button addnewBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        addnewBin = (Button) findViewById(R.id.addNewBinBtn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MrBin");

        addnewBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent binIntent = new Intent(MainActivity.this,AddBinActivity.class);
                startActivity(binIntent);

            }
        });

        if (mAuth.getCurrentUser() != null) {

            mUserRef = FirebaseDatabase.getInstance().getReference().child("App_Users").child(mAuth.getCurrentUser().getUid());

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            sendToStart();
        }
    }

    private void sendToStart() {
        Intent intent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.main_logout_button){
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            FirebaseAuth.getInstance().signOut();
            sendToStart();

        }
        if(item.getItemId() == R.id.main_account_Button){
            Intent accountsettings = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(accountsettings);
        }
        if(item.getItemId() == R.id.report_button){
            Intent report = new Intent(MainActivity.this,ReportActivity.class);
            startActivity(report);
        }
        return super.onOptionsItemSelected(item);
    }
}
