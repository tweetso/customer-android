package com.experto.experto.Activities.Authentication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.Activities.AppPages.ExpertoLoading;
import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class ChangePhone extends AppCompatActivity {

    private static final String TAG = "number";
    private Button sendCodeButton;
    private Button saveButton;
    private EditText phoneNumber;
    private EditText verifyingCode;
    private TextView missingFieldsM;
    private FirebaseAuth mAuth;
    private View.OnClickListener clickListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String codeSent;
    private ProgressDialog progressDialog;
    private PhoneAuthCredential credential;
    private String number;
    private DocumentReference dRef;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        sendCodeButton = (Button) findViewById(R.id.send_code_button);
        saveButton = (Button) findViewById(R.id.save_number);
        phoneNumber = (EditText) findViewById(R.id.number_edit_change_phone);
        verifyingCode = (EditText) findViewById(R.id.verify_number_edit_change_phone);
        missingFieldsM = (TextView) findViewById(R.id.missing_change_phone_nubmber_fields_m);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dRef = db.collection("customer").document(mAuth.getCurrentUser().getUid());
        progressDialog = new ProgressDialog(this);
        initializeClickListener();
        sendCodeButton.setOnClickListener(clickListener);
        saveButton.setOnClickListener(clickListener);
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
                    // [START_EXCLUDE]
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
                    if( !missingFieldsM.getText().toString().equals("")){
                        missingFieldsM.setText("");
                    }
                    String countryName = Customer.getInstance().getCountry();
                    number = ExpertoLoading.countriesList.get(countryName)+phoneNumber.getText().toString();

                    if (number.equals("")) {
                        missingFieldsM.setText("Phone number is required");
                    }
                    else {
                        startPhoneNumberVerification(number);
                        sendCodeButton.setText("Resend code");
                        saveButton.setVisibility(View.VISIBLE);
                        verifyingCode.setVisibility(View.VISIBLE);
                    }
                }
                if (view.getId() == R.id.save_number){
                    if( !missingFieldsM.getText().toString().equals("")){
                        missingFieldsM.setText("");
                    }
                    String codeEntered = verifyingCode.getText().toString();
                    if(codeEntered.equals("")){
                        missingFieldsM.setText("Verifying code is required");
                    }
                    else {
                        credential = PhoneAuthProvider.getCredential(codeSent,codeEntered);
                        mAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(ChangePhone.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithCredential:success");
                                    Customer.getInstance().setPhone(number);
                                    dRef.update("phone", number)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ChangePhone.this, "Inforamtion get updated successfully", Toast.LENGTH_SHORT).show();
                                                    Customer.getInstance().setPhone(number);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ChangePhone.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    finish();

                                } else {
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        missingFieldsM.setText("Invalid code.");
                                    } else {
                                        Toast.makeText(ChangePhone.this,"signInWithCredential:failure"+task.getException(),
                                                Toast.LENGTH_LONG).show();
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

}
