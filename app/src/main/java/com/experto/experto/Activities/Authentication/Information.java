package com.experto.experto.Activities.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.Activities.AppPages.Home;
import com.experto.experto.R;
import com.experto.experto.Activities.AppPages.RequestActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Information extends AppCompatActivity {

    private EditText fullNameEdit;
    private TextView countryShow;
    private TextView missingInfo;
    private EditText cityEdit;
    private Spinner genderList;
    private Button saveInfo;
    private View.OnClickListener clickListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Boolean requestMade =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        fullNameEdit = (EditText)findViewById(R.id.full_name_edit);
        cityEdit = (EditText)findViewById(R.id.city_edit);
        countryShow = (TextView)findViewById(R.id.country_show);
        genderList = (Spinner)findViewById(R.id.gender_choice);
        saveInfo = (Button)findViewById(R.id.save_user_information_button);
        missingInfo = (TextView)findViewById(R.id.missing_inforamtion_fields_m);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        if(extras !=null){
            countryShow.setText(extras.getString("country"));
        }
        initializeClickListener();
        saveInfo.setOnClickListener(clickListener);
        // to know whether the sign up is from sending request or not
        if(getIntent().getExtras() !=null) {
            requestMade = getIntent().getExtras().getBoolean("request");
        }
    }

    public void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.save_user_information_button) {
                    String fullName = fullNameEdit.getText().toString();
                    String city = cityEdit.getText().toString();
                    String country = countryShow.getText().toString();
                    String gender = genderList.getSelectedItem().toString();
                    if(!fullName.equals("") && !city.equals("")){
                        if( !missingInfo.getText().toString().equals("")){
                            missingInfo.setText("");
                        }
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user!=null){
                            Customer.getInstance().setEmail(user.getEmail());
                            Customer.getInstance().setPhone(user.getPhoneNumber());
                            Customer.getInstance().setName(fullName);
                            Customer.getInstance().setCountry(country);
                            Customer.getInstance().setCity(city);
                            Customer.getInstance().setGender(gender);
                            Customer.getInstance().setId(user.getUid());
                            db.collection("customer").document(user.getUid()).set(Customer.getInstance()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Information.this,"The account has been created",Toast.LENGTH_SHORT).show();
                                    if(requestMade){
                                        Intent intent = new Intent(Information.this, RequestActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(Information.this,Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Customer.getInstance().setCountry(null);
                                    Customer.getInstance().setCity(null);
                                    Customer.getInstance().setGender(null);
                                    Toast.makeText(Information.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                    else {
                        missingInfo.setText("You did not fill all the fields");
                    }
                }
            }

        };
    };

    protected void onDestroy() {
        super.onDestroy();
        if(Customer.getInstance()==null){
            if(mAuth.getCurrentUser() !=null){
                mAuth.signOut();
            }
        }
    }
}
