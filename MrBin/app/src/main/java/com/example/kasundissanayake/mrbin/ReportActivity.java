package com.example.kasundissanayake.mrbin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static com.example.kasundissanayake.mrbin.R.id.parent;
import static com.example.kasundissanayake.mrbin.R.id.spinner3;

public class ReportActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private EditText mMessage;
    private Button msendMessage;
    private ProgressDialog mProgressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mAuth = FirebaseAuth.getInstance();

        mToolBar = (Toolbar) findViewById(R.id.report_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report");

        mMessage = (EditText) findViewById(R.id.editMessageText);
        msendMessage = (Button) findViewById(R.id.sendMessageBtn);
        //get the spinner from the xml.
        final Spinner dropdown = (Spinner) findViewById(R.id.spinner3);
//create a list of items for the spinner.
        String[] items = new String[]{"Collecting Issue", "Garbage Bin Issue", "Other"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        mProgressBar = new ProgressDialog(this);

        msendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                mProgressBar.setTitle("Reporting Issues");
                mProgressBar.setMessage("Please wait while we send your message to the server");
                mProgressBar.setCanceledOnTouchOutside(false);
                mProgressBar.show();

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                String subject = dropdown.getSelectedItem().toString();


                String uId = currentUser.getUid();
                String message = mMessage.getText().toString();
                String telephone = "0112234567";
                String bin_id = "bin_1";
                String feedback_id = random();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("feedback_details").child(feedback_id);

                HashMap<String, String> userMap = new HashMap<String, String>();
                userMap.put("user_id", uId);
                userMap.put("subject", subject);
                userMap.put("feedback", message);
                userMap.put("status", "unsolved");
                userMap.put("date_of_feedback", formattedDate);
                mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgressBar.dismiss();
                            Toast.makeText(ReportActivity.this, "Your Message successfully sent", Toast.LENGTH_LONG).show();


                        }else

                        {
                            mProgressBar.hide();
                            Toast.makeText(ReportActivity.this, "Something Wrong", Toast.LENGTH_LONG).show();

                        }
                    }
                });




            }
        });
    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
