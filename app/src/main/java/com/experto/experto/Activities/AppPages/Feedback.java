package com.experto.experto.Activities.AppPages;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.experto.experto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Feedback extends AppCompatActivity {

    private Spinner feedbackTypes;
    private EditText feedback;
    private Button send;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationmenu;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    String[] types = new String[]{
            "Suggestion",
            "Compliant",
            "Request",
            "Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackTypes = (Spinner) findViewById(R.id.feedback_types);
        // to fill the spinner with list of types
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,types);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        feedbackTypes.setAdapter(spinnerArrayAdapter);
        feedback = (EditText)findViewById(R.id.feedback);
        send = (Button)findViewById(R.id.send_feedback);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        navigationmenu = (NavigationView) findViewById(R.id.navigation_menu);
        // start making the navigation menu button
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer_layout,R.string.close_drawer_layout);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // end making the navigation menu button
        onNavigationItemSelectedListener= Home.initializeNavigationMenuListener(this,drawerLayout,navigationmenu,
                onNavigationItemSelectedListener,user);
        navigationmenu.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        db= FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedbackeEntered = feedback.getText().toString();
                String type = feedbackTypes.getSelectedItem().toString();
                HashMap<String,String> data = new HashMap<>();
                if(!feedbackeEntered.equals("")){
                    data.put("message",feedbackeEntered);
                    data.put("type",type);
                    if(user!=null) {
                        data.put("userEmail", user.getEmail().toString());
                    }
                    else {
                        data.put("userEmail","");
                    }
                    progressDialog.setMessage("Sending");
                    progressDialog.show();
                        db.collection("feedback")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(Feedback.this,"Feedback has been send, thank you",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Feedback.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                }
                else {
                    Toast.makeText(Feedback.this,"The feedback is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
