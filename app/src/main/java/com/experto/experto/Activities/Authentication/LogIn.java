package com.experto.experto.Activities.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.experto.experto.R;

public class LogIn extends AppCompatActivity {

    private Boolean requestMade =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        requestMade = getIntent().getExtras().getBoolean("request");
    }

    public void openSignIn(View view){
        Intent i = new Intent(this,SignIn.class);
        i.putExtra("request",requestMade);
        startActivity(i);
    }
    public void openSignUp(View view){
        Intent i = new Intent(this,SignUp.class);
        i.putExtra("request",requestMade);
        startActivity(i);
    }
}
