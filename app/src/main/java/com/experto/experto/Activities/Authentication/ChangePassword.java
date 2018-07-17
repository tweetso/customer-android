package com.experto.experto.Activities.Authentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private EditText currentPassword;
    private EditText newPassword;
    private Button saveButton;
    private TextView passwordProblem;
    private View.OnClickListener clickListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPassword = (EditText) findViewById(R.id.current_password_edit);
        newPassword = (EditText) findViewById(R.id.new_password_edit);
        saveButton = (Button)findViewById(R.id.save_password_button);
        passwordProblem = (TextView)findViewById(R.id.password_problem_m);
        initializeClickListener();
        saveButton.setOnClickListener(clickListener);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void initializeClickListener (){
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.save_password_button) {
                    if(user!=null) {
                        String email = Customer.getInstance().getEmail();
                        String cPassword = currentPassword.getText().toString();
                        final String nPasswrod = newPassword.getText().toString();
                        if(cPassword.equals("")){
                            passwordProblem.setText("Current password are required");
                        }
                        else if(nPasswrod.equals("")){
                            passwordProblem.setText("New password are required");
                        }
                        else{
                            passwordProblem.setText("");
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, cPassword);
                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user.updatePassword(nPasswrod).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(ChangePassword.this,"Password updated",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {

                                                            Toast.makeText(ChangePassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                String errorMessage = "Authentication failed, your current password may be wrong, please renter your current password";
                                                Toast.makeText(ChangePassword.this,errorMessage,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        };
    }
}
