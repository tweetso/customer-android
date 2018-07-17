package com.experto.experto.Activities.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView missingFieldsM;
    private Button continueButton;
    private View.OnClickListener clickListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    HashMap<String,Object> infoList = new HashMap<String,Object>();
    private Boolean requestMade =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailEdit = findViewById(R.id.email_edit_sign_up);
        passwordEdit = findViewById(R.id.password_edit_sign_up);
        missingFieldsM = findViewById(R.id.missing_sgin_up_fields_m);
        continueButton = findViewById(R.id.continue_button_1);
        mAuth = FirebaseAuth.getInstance();
        initializeClickListener();
        continueButton.setOnClickListener(clickListener);
        if(getIntent().getExtras() !=null) {
            requestMade = getIntent().getExtras().getBoolean("request");
        }
    }

    public void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.continue_button_1) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        final String email = emailEdit.getText().toString();
                        final String password = passwordEdit.getText().toString();
                        ;
                        if (!email.equals("") && !password.equals("")) {
                            if( !missingFieldsM.getText().toString().equals("")){
                                missingFieldsM.setText("");
                            }
                            infoList.put("email", email);
                            infoList.put("password", password);
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("signup", "createUserWithEmail:success");
                                                Intent intent = new Intent(SignUp.this, MobileRegistration.class);
                                                intent.putExtra("request",requestMade);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            // ...
                                        }
                                    });
                        } else {
                            missingFieldsM.setText("You did not fill all the fields");
                        }
                    } else {
                        Toast.makeText(SignUp.this, "You already signed in", Toast.LENGTH_SHORT).show();
                    }
                }
                    }

            };
        };
}
