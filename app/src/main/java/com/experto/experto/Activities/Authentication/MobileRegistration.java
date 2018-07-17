package com.experto.experto.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.Activities.AppPages.ExpertoLoading;
import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MobileRegistration extends AppCompatActivity {

    private static final String TAG = "number";
    private HashMap<String, Object> infoList;
    private Button sendCodeButton;
    private Button continueButton;
    private EditText phoneNumber;
    private EditText verifyingCode;
    private TextView missingFieldsM;
    private Spinner countryNames;
    private FirebaseAuth mAuth;
    private View.OnClickListener clickListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String codeSent;
    private ProgressDialog progressDialog;
    private PhoneAuthCredential credential;
    private Boolean requestMade =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_registration);
        sendCodeButton = (Button) findViewById(R.id.send_code_button);
        continueButton = (Button) findViewById(R.id.continue_button_2);
        phoneNumber = (EditText) findViewById(R.id.number_edit_sign_up);
        verifyingCode = (EditText) findViewById(R.id.verify_number_edit_sign_up);
        missingFieldsM = (TextView) findViewById(R.id.missing_sgin_up_nubmber_fields_m);
        countryNames = (Spinner) findViewById(R.id.country_names);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        infoList = (HashMap<String, Object>) getIntent().getSerializableExtra("infoList");
        sendCodeButton.setOnClickListener(clickListener);
        continueButton.setOnClickListener(clickListener);
        if(getIntent().getExtras() !=null) {
            requestMade = getIntent().getExtras().getBoolean("request");
        }
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("number", "onVerificationCompleted:" + credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    missingFieldsM.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    //                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent =s;
                progressDialog.dismiss();
            }
        };


    }
    public void initializeClickListener () {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.send_code_button) {
                    String countryName = countryNames.getSelectedItem().toString();
                    String number = ExpertoLoading.countriesList.get(countryName)+phoneNumber.getText().toString();

                    if (number.equals("")) {
                        missingFieldsM.setText("Phone number is required");
                    }
                    else {
                        if( !missingFieldsM.getText().toString().equals("")){
                            missingFieldsM.setText("");
                        }
                        startPhoneNumberVerification(number);
                        sendCodeButton.setText("Resend code");
                        continueButton.setVisibility(View.VISIBLE);
                        verifyingCode.setVisibility(View.VISIBLE);
                    }
                }
                if (view.getId() == R.id.continue_button_2){
                    String codeEntered = verifyingCode.getText().toString();
                    if(codeEntered.equals("")){
                        missingFieldsM.setText("Verifying code is required");
                    }
                    else {
                        if( !missingFieldsM.getText().toString().equals("")){
                            missingFieldsM.setText("");
                        }
                        credential = PhoneAuthProvider.getCredential(codeSent,codeEntered);
                        mAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(MobileRegistration.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithCredential:success");

                                    Toast.makeText(MobileRegistration.this,"Mobile Verified Successfully.",Toast.LENGTH_SHORT).show();
                                    //Open Information activity
                                    Intent intent = new Intent(MobileRegistration.this,Information.class);
                                    intent.putExtra("country",countryNames.getSelectedItem().toString());
                                    intent.putExtra("request",requestMade);
                                    startActivity(intent);

                                } else {
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        Toast.makeText(MobileRegistration.this,"Invalid Code.",Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(MobileRegistration.this,task.getException().getMessage(),
                                                Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                        Log.d(TAG,credential.toString());
                    }
                }
            }
        };
    };
    private void startPhoneNumberVerification (String phoneNumber){
        // [START start_phone_auth]
        progressDialog.setMessage("Sending please wait");
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Customer.getInstance()==null){
            if(mAuth.getCurrentUser() !=null){
                mAuth.signOut();
            }
        }
    }
}
