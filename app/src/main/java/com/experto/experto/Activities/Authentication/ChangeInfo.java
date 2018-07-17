package com.experto.experto.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class ChangeInfo extends AppCompatActivity {
    private EditText info;
    private TextView emptyInfo;
    private EditText cityEdit;
    private Button save;
    private Spinner countriesList;
    private FrameLayout countryContainer;
    private View.OnClickListener clickListener;
    private DocumentReference dRef;
    private FirebaseFirestore db;
    private String key;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        info = (EditText)findViewById(R.id.info_edit);
        emptyInfo = (TextView)findViewById(R.id.empty_m);
        save = (Button)findViewById(R.id.save_info_button);
        cityEdit = (EditText) findViewById(R.id.city_change_edit);
        countriesList = (Spinner)findViewById(R.id.country_names);
        countryContainer= (FrameLayout) findViewById(R.id.country_container);
        builder = new AlertDialog.Builder(ChangeInfo.this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dRef = db.collection("customer").document(mAuth.getCurrentUser().getUid());
        progressDialog = new ProgressDialog(this);
        initializeClickListener();
        save.setOnClickListener(clickListener);
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");
        if(key.equals("address")){
            countryContainer.setVisibility(View.VISIBLE);
            info.setVisibility(View.INVISIBLE);
            cityEdit.setVisibility(View.VISIBLE);
        }

        switch (key) {
            case "name":
                info.setText(Customer.getInstance().getName());
                break;
            case "email":
                info.setText(Customer.getInstance().getEmail());
                break;
            case "gender":
                info.setText(Customer.getInstance().getGender());
                break;
            case "address":
                countriesList.setSelection(((ArrayAdapter) countriesList.getAdapter()).getPosition(Customer.getInstance().getCountry()));
                cityEdit.setText(Customer.getInstance().getCity());
                break;
        }
    }

    public void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.save_info_button:
                        updateInfo();
                        break;
                }
            }

            ;
        };
    }

    public void updateInfo(){
        final String updatedInfo = info.getText().toString();
        if(key.equals("address")) {
            final String updatedCity = cityEdit.getText().toString();
            final String country = countriesList.getSelectedItem().toString();
            if(!updatedCity.equals("")){
                if( !emptyInfo.getText().toString().equals("")){
                    emptyInfo.setText("");
                }
                progressDialog.setMessage("Updating");
                progressDialog.show();
                WriteBatch batch = db.batch();
                dRef.update("country", country,"city",updatedCity)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ChangeInfo.this, "Inforamtion get updated successfully", Toast.LENGTH_SHORT).show();
                                Customer.getInstance().setCountry(country);
                                Customer.getInstance().setCity(updatedCity);
                                progressDialog.dismiss();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangeInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
            }
            else {
                emptyInfo.setText("You did not fill all the fields");
            }
        }
        else if(key.equals("email")){
            if (!updatedInfo.equals("")) {
                if( !emptyInfo.getText().toString().equals("")){
                    emptyInfo.setText("");
                }
                progressDialog.setMessage("Updating");
                progressDialog.show();
                mAuth.getCurrentUser().updateEmail(updatedInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dRef.update("email", updatedInfo)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ChangeInfo.this, "Inforamtion get updated successfully", Toast.LENGTH_SHORT).show();
                                                    Customer.getInstance().setEmail(updatedInfo);
                                                    progressDialog.dismiss();
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ChangeInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                }
                                else {
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

            } else {
                emptyInfo.setText("You did not fill all the fields");
            }
        }
        else {
            if (!updatedInfo.equals("")) {
                if( !emptyInfo.getText().toString().equals("")){
                    emptyInfo.setText("");
                }
                progressDialog.setMessage("Updating");
                progressDialog.show();
                dRef.update(key, updatedInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ChangeInfo.this, "Inforamtion get updated successfully", Toast.LENGTH_SHORT).show();
                                switch (key) {
                                    case "name":
                                        Customer.getInstance().setName(updatedInfo);
                                        break;
                                    case "email":
                                        Customer.getInstance().setEmail(updatedInfo);
                                        break;
                                    case "gender":
                                        Customer.getInstance().setGender(updatedInfo);
                                        break;
                                }
                                progressDialog.dismiss();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(ChangeInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                emptyInfo.setText("You did not fill all the fields");
            }
        }
    }
}
