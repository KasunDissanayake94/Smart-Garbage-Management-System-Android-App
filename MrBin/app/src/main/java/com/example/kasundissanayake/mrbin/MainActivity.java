package com.example.kasundissanayake.mrbin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DatabaseReference mUserRef;
    private ProgressBar progress;
    private ImageView processImage;
    private ImageView binImage;
    private Button addnewBin;
    private int count = 1;
    private ProgressDialog mProgressBar;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private DatabaseReference mDatabase2;
    private FirebaseUser mcurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mProgressBar = new ProgressDialog(this);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        addnewBin = (Button) findViewById(R.id.addNewBinBtn);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        binImage = (ImageView) findViewById(R.id.emptyBinImage);
        processImage = (ImageView) findViewById(R.id.processImage);

        //Setting the topic of the Activity
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MrBin");
        progress.setProgress(0);
        //Check whether user request has been solved or not.If it solved change the images and progressBar
        mcurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mcurrentUser != null){
            String current_id = mcurrentUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Bin_requests").child(current_id);
            mDatabase.keepSynced(true);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String status = dataSnapshot.child("status").getValue().toString();
                    if (status.equals("solved")){
                        processImage.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        binImage.setVisibility(View.VISIBLE);
                        addnewBin.setEnabled(true);
                        addnewBin.setText("REMOVE BIN");
                        addnewBin.setBackgroundColor(Color.RED);
                        count = 0;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mDatabase1 = FirebaseDatabase.getInstance().getReference().child("App_Users").child(current_id);
            mDatabase1.keepSynced(true);
            mDatabase1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String bin_id = dataSnapshot.child("bin_id").getValue().toString();
                    if (!bin_id.equals("null")){
                        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("bin").child(bin_id);
                        mDatabase2.keepSynced(true);
                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String garbage_level = dataSnapshot.child("level").getValue().toString();
                                if (garbage_level.equals("high")){
                                    progress.setProgress(100);
                                }else if(garbage_level.equals("medium")){
                                    progress.setProgress(60);
                                }else if(garbage_level.equals("low")){
                                    progress.setProgress(20);
                                }else{
                                    progress.setProgress(0);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        //Check the current garbage level and update the progess bar
        //Get the User's assign Bin_id from the App_Users table


        //If User Clicks the Add Bin or Remove Button
        addnewBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setTitle("Sending Request");
                mProgressBar.setMessage("Please wait while we send your request");
                mProgressBar.setCanceledOnTouchOutside(false );
                mProgressBar.show();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String uId = currentUser.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Bin_requests").child(uId);
                if (count == 1){
                    HashMap<String,String> userMap = new HashMap<String, String>();
                    userMap.put("type","request");
                    userMap.put("status","not solved");
                    count = 0;
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mProgressBar.dismiss();
                                addnewBin.setEnabled(false);
                                addnewBin.setBackgroundColor(Color.RED);
                                addnewBin.setText("REMOVE BIN");

                            }
                        }
                    });
                }else if(count == 0){
                    HashMap<String,String> userMap = new HashMap<String, String>();
                    userMap.put("type","remove");
                    userMap.put("status","not solved");
                    count = 1;
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                mProgressBar.dismiss();
                                addnewBin.setText("ADD NEW BIN");
                                addnewBin.setEnabled(false);

                            }
                        }
                    });
                }


            }
        });

        if (mAuth.getCurrentUser() != null) {

            mUserRef = FirebaseDatabase.getInstance().getReference().child("App_Users").child(mAuth.getCurrentUser().getUid());

        }
    }
    //Check the The Current User
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            sendToStart();
        }
    }
    //Maintain the Menu Items those the user hits
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
