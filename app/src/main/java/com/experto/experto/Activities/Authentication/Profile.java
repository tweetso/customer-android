package com.experto.experto.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.experto.experto.Activities.AppPages.Home;
import com.experto.experto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Profile extends AppCompatActivity {
    private static final int GALLERY_INTENT = 2;
    private TextView name;
    private TextView number;
    private TextView email;
    private TextView gender;
    private TextView address;
    private CardView nameCard;
    private CardView numberCard;
    private CardView emailCard;
    private CardView genderCard;
    private CardView addressCard;
    private CardView passwordCard;
    private CardView logout;
    private ImageView profileImage;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationmenu;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private FirebaseAuth mAuth;
    private static final String TAG ="Login";
    private View.OnClickListener clickListener;
    private ImageView changeImage;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private StorageReference filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        //Setting values to the activity fields
        name = (TextView)findViewById(R.id.name_value);
        number = (TextView)findViewById(R.id.mobile_vaule);
        email = (TextView)findViewById(R.id.email_vaule);
        gender = (TextView)findViewById(R.id.gender_vaule);
        address = (TextView)findViewById(R.id.address_vaule);
        logout = (CardView) findViewById(R.id.logout_card);
        changeImage= (ImageView) findViewById(R.id.change_image);
        profileImage = (ImageView)findViewById(R.id.profile_image);
        nameCard = (CardView) findViewById(R.id.name_card);
        numberCard = (CardView) findViewById(R.id.mobile_card);
        emailCard = (CardView) findViewById(R.id.email_card);
        genderCard = (CardView) findViewById(R.id.gender_card);
        addressCard = (CardView) findViewById(R.id.address_card);
        passwordCard = (CardView) findViewById(R.id.password_card);
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
        initializeClickListener();
        nameCard.setOnClickListener(clickListener);
        numberCard.setOnClickListener(clickListener);
        emailCard.setOnClickListener(clickListener);
        genderCard.setOnClickListener(clickListener);
        addressCard.setOnClickListener(clickListener);
        passwordCard.setOnClickListener(clickListener);
        logout.setOnClickListener(clickListener);
        changeImage.setOnClickListener(clickListener);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        if(Customer.getInstance() != null){
            name.setText(Customer.getInstance().getName());
            number.setText(Customer.getInstance().getPhone());
            email.setText(Customer.getInstance().getEmail());
            gender.setText(Customer.getInstance().getGender());
            address.setText(Customer.getInstance().getCountry()+" "+Customer.getInstance().getCity());
            String imageUrl = Customer.getInstance().getImage();
            Glide.with(getApplicationContext()).load(imageUrl).into(profileImage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Customer.getInstance() != null){
            name.setText(Customer.getInstance().getName());
            number.setText(Customer.getInstance().getPhone());
            email.setText(Customer.getInstance().getEmail());
            gender.setText(Customer.getInstance().getGender());
            address.setText(Customer.getInstance().getCountry()+" "+Customer.getInstance().getCity());
            String imageUrl = Customer.getInstance().getImage();
            Glide.with(getApplicationContext()).load(imageUrl).into(profileImage); }
    }

    public void initializeClickListener() {
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.logout_card:
                        signout();
                        break;
                    case R.id.name_card:
                        openChangeInfoActivity("name");
                        break;
                    case R.id.mobile_card:
                        Intent i = new Intent(Profile.this,ChangePhone.class);
                        startActivity(i);
                        break;
                    case R.id.email_card:
                        openChangeInfoActivity("email");
                        break;
                    case R.id.gender_card:
                        openChangeInfoActivity("gender");
                        break;
                    case R.id.address_card:
                        openChangeInfoActivity("address");
                        break;
                    case R.id.password_card:
                        Intent intent = new Intent(Profile.this,ChangePassword.class);
                        startActivity(intent);
                        break;
                    case R.id.change_image:
                        uploadImage();
                        break;
                }
            };
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            if(mAuth.getCurrentUser() != null) {
                progressDialog.setMessage("Uploading");
                progressDialog.show();
                Uri uri = data.getData();
                filePath = mStorage.child("customer").child(mAuth.getCurrentUser().getUid()).child("image");
                Log.d(TAG,filePath.getDownloadUrl().toString());
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                docRef.update("image", uri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                Glide.with(getApplicationContext()).load(uri.toString()).into(profileImage);
                                                Customer.getInstance().setImage(uri.toString());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });
                            }
                        });
                        Toast.makeText(Profile.this,"Upload done",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        }
    }

    public void signout(){
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
            Customer.setInstance(null);
            Toast.makeText(Profile.this,"sign out success",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Profile.this,Home.class);
            finish();
            startActivity(i);
        }
        else {
            Toast.makeText(Profile.this,"No user",Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }

    public void openChangeInfoActivity(String key){
        Intent intent = new Intent(Profile.this, ChangeInfo.class);
        intent.putExtra("key", key);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
