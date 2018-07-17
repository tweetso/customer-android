package com.experto.experto.Activities.AppPages;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.experto.experto.Activities.Authentication.Customer;
import com.experto.experto.Activities.Authentication.SignIn;
import com.experto.experto.AppData.SKU;
import com.experto.experto.AppData.Service;
import com.experto.experto.AppData.ServiceA;
import com.experto.experto.AppData.ServiceB;
import com.experto.experto.AppData.Company;
import com.experto.experto.AppData.Device;
import com.experto.experto.AppData.Post;
import com.experto.experto.AppData.Problem;
import com.experto.experto.ListItems.ProblemImageItem;
import com.experto.experto.ListItems.RequestItem;
import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpertoLoading extends AppCompatActivity {

    private ProgressBar loading;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore db;
    private DocumentReference docRef;
    private static List<Service> menuList = new ArrayList<>();
    private static HashMap<String,SKU> skuList = new HashMap<>();
    public static HashMap<String,String> countriesList = new HashMap<>();
    private static HashMap<Long,String> RequestStatesList = new HashMap<>();
    private static List<Post> postList = new ArrayList<>();
    private static List<RequestItem> requestCompletedItemsList = new ArrayList<>();
    private static List<RequestItem> requestInProgressItemsList = new ArrayList<>();
    private  FetchingData fetchData;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experto_loading);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        builder = new AlertDialog.Builder(ExpertoLoading.this);
        // fetch the json file
        fetchData = new FetchingData("https://tweetso-865d9.firebaseapp.com/service.json");
        fetchData.execute();
        // get the customer info from database
        fillCustomerInfo();
        // initialize Countries List for the mobile registration
        initializeCountriesList();
        // initialize Request States List for the Requests Activity
        initializeRequestStatesList();
        readingPosts();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fillCustomerInfo(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            docRef = db.collection("customer").document(user.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Customer.setInstance(documentSnapshot.toObject(Customer.class));
                }
            });
        }
    }

    public static List<Service> getMenuList() {
        return menuList;
    }

    public static HashMap<String, SKU> getSkuList() {
        return skuList;
    }

    public static List<Post> getPostList() {
        return postList;
    }

    public static HashMap<Long, String> getRequestStatesList() {
        return RequestStatesList;
    }

    public static List<RequestItem> getRequestInProgressItemsList() {
        return requestInProgressItemsList;
    }

    public static List<RequestItem> getRequestCompletedItemsList() {
        return requestCompletedItemsList;
    }

    public static void setRequestCompletedItemsList(List<RequestItem> requestCompletedItemsList) {
        ExpertoLoading.requestCompletedItemsList = requestCompletedItemsList;
    }

    public static void setRequestInProgressItemsList(List<RequestItem> requestInProgressItemsList) {
        ExpertoLoading.requestInProgressItemsList = requestInProgressItemsList;
    }

    public void initializeCountriesList(){
        countriesList.put("Bahrain","+973");
        countriesList.put("Comoros","+269");
        countriesList.put("Djibouti","+253");
        countriesList.put("Egypt","+20");
        countriesList.put("Iraq","+964");
        countriesList.put("Jordan","+962");
        countriesList.put("Kuwait","+965");
        countriesList.put("Lebanon","+961");
        countriesList.put("Libya","+218");
        countriesList.put("Mauritania","+222");
        countriesList.put("Morocco","+212");
        countriesList.put("Oman","+968");
        countriesList.put("Palestine","+970");
        countriesList.put("Qatar","+974");
        countriesList.put("Saudi Arabia","+966");
        countriesList.put("Somalia","+252");
        countriesList.put("Sudan","+249");
        countriesList.put("Syria","+963");
        countriesList.put("Tunisia","+216");
        countriesList.put("United Arab Emirates","+971");
        countriesList.put("Yemen","+967");
    }

    public void fillSKUs(){
        skuList = new HashMap<>();
        for(int i=0; i<menuList.size(); i++){
            if(menuList.get(i).getClass() == ServiceA.class){
                ServiceA serviceA = (ServiceA) menuList.get(i);
                for(int j=0; j<serviceA.getCompanies().size(); j++){
                    Company company = serviceA.getCompanies().get(j);
                    for(int k=0; k<company.getDevices().size(); k++){
                        Device device = company.getDevices().get(k);
                        for(int x =0; x<device.getProblems().size(); x++){
                            Problem problem= device.getProblems().get(x);
                            SKU sku = new SKU(company.getName(),device.getName(),
                                    serviceA.getName(),problem.getName(),problem.getServiceFee(),problem.getPartFee());
                            skuList.put(problem.getSku(),sku);
                        }
                    }
                }
            }
            else if(menuList.get(i).getClass() == ServiceB.class){
                ServiceB serviceB = (ServiceB) menuList.get(i);
                for(int j=0; j<serviceB.getProblems().size(); j++){
                    Problem problem= serviceB.getProblems().get(j);
                    SKU sku = new SKU(serviceB.getName(),"",
                            serviceB.getName(),problem.getName(),problem.getServiceFee(),problem.getPartFee());
                    skuList.put(problem.getSku(),sku);
                }
            }
        }
    }
    public void initializeRequestStatesList(){
        RequestStatesList.put((long)0,"Assigning technician...");
        RequestStatesList.put((long)6,"At repair shop");
    }

    public void readingPosts(){
        postList = new ArrayList<>();
        final CollectionReference request = db.collection("posts");
        request.orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()){
                       Post post = document.toObject(Post.class);
                       postList.add(post);
                    }
                }
            }
        });
    }

    public static void getRequestsFromDB(Context context){
        RequestsInProgressTab.getInProgressRequestsFromDB(mAuth,db,context);
        RequestsCompletedTab.getCompletedRequestsFromDB(mAuth,db,context);
    }

    public class FetchingData extends AsyncTask<Void,Void,Void> {
        private String urlString;
        private String data="";
        private JSONArray jsonArray;
        public FetchingData(String url){
            urlString =url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = (ProgressBar)findViewById(R.id.home_loading);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BufferedReader bufferedReader;
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection= (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line ="";
                Log.d("Main","done1");
                while (line !=null){
                    line = bufferedReader.readLine();
                    data += line;
                }
                bufferedReader.close();
                Log.d("Main","done2");
                jsonArray = new JSONArray(data);
                Log.d("Main","done3");
            }

            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public JSONArray getJsonArray() {
            return jsonArray;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jsonArray = fetchData.getJsonArray();
            // free the list to have a new list whenever the app lunch and to avoid duplicates
            menuList = new ArrayList<>();
            try {
                if(jsonArray!=null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.get("name").equals("Support") || jsonObject.get("name").equals("Computer")) {
                            ServiceB b = new Gson().fromJson(jsonArray.get(i).toString(), ServiceB.class);
                            menuList.add(b);
                        } else {
                            ServiceA a = new Gson().fromJson(jsonArray.get(i).toString(), ServiceA.class);
                            menuList.add(a);
                        }
                    }
                    // Fill SKU list to get the name of the company and the device when retrieve the SKUs from Database
                    fillSKUs();
                    getRequestsFromDB(ExpertoLoading.this);
                    loading.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(ExpertoLoading.this,Home.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    builder.setMessage("Could not load date, Please check your Internet connection")
                            .setTitle("Message").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    }).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
