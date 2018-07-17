package com.experto.experto.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.Activities.AppPages.ExpertoLoading;
import com.experto.experto.Activities.AppPages.RequestActivity;
import com.experto.experto.Activities.AppPages.Home;
import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView missingFieldsM;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationmenu;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private Boolean requestMade =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        emailEdit = findViewById(R.id.email_edit_sign_in);
        passwordEdit = findViewById(R.id.password_edit_sign_in);
        missingFieldsM = findViewById(R.id.missing_fields_m);
        navigationmenu = (NavigationView) findViewById(R.id.navigation_menu);
        // start making the navigation menu button
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer_layout,R.string.close_drawer_layout);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // end making the navigation menu button
        onNavigationItemSelectedListener= Home.initializeNavigationMenuListener(this,drawerLayout,navigationmenu,
                onNavigationItemSelectedListener,mAuth.getCurrentUser());
        navigationmenu.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        db = FirebaseFirestore.getInstance();
        builder = new AlertDialog.Builder(SignIn.this);
        progressDialog = new ProgressDialog(this);
        if(getIntent().getExtras() !=null) {
            requestMade = getIntent().getExtras().getBoolean("request");
        }
    }

    public void openSignUp(View view){
        Intent i = new Intent(this,SignUp.class);
        i.putExtra("request",requestMade);
        startActivity(i);
    }

    public void signIn(View view){
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();;
        if(!email.equals("")&& !password.equals("")){
            if( !missingFieldsM.getText().toString().equals("")){
                missingFieldsM.setText("");
            }
            progressDialog.setMessage("signing in");
            progressDialog.show();
            signInWithFireBase(email,password);
        }
        else {
            missingFieldsM.setText("You did not fill all the fields");
        }
    }

    public void signInWithFireBase(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            docRef = db.collection("customer").document(user.getUid());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Toast.makeText(SignIn.this, "Sign in success wit email " +user.getEmail(),
                                            Toast.LENGTH_SHORT).show();
                                    Customer.setInstance(documentSnapshot.toObject(Customer.class));
                                    ExpertoLoading.getRequestsFromDB(SignIn.this);
                                    progressDialog.dismiss();
                                    if(Customer.getInstance()==null) {
                                        if (mAuth.getCurrentUser().getPhoneNumber() == null) {
                                            Intent intent = new Intent(SignIn.this, MobileRegistration.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Intent intent = new Intent(SignIn.this, Information.class);
                                            startActivity(intent);
                                        }
                                    }
                                    if(requestMade){
                                        Intent intent = new Intent(SignIn.this, RequestActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(SignIn.this,Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    builder.setMessage(e.getMessage())
                                            .setTitle("Message").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    }).show();
                                    mAuth.signOut();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            builder.setMessage(task.getException().getMessage())
                                    .setTitle("Message").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }).show();
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
