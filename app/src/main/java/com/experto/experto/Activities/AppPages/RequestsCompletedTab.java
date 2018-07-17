package com.experto.experto.Activities.AppPages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.experto.experto.Activities.AppPages.ExpertoLoading;
import com.experto.experto.Adapters.ProblemImageItemsAdapter;
import com.experto.experto.Adapters.RequestItemAdapter;
import com.experto.experto.AppData.SKU;
import com.experto.experto.ListItems.ProblemImageItem;
import com.experto.experto.ListItems.RequestItem;
import com.experto.experto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RequestsCompletedTab extends Fragment {

    private static ProblemImageItemsAdapter imagesAdapter;
    private RequestItemAdapter requestsAdapter;
    private static List<ProblemImageItem> problemImagesList;
    private GridView requests;
    private List<RequestItem> requestItemsList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.requests_in_progress_tab,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        requests = (GridView) getView().findViewById(R.id.requests_grid);
        requestItemsList = ExpertoLoading.getRequestCompletedItemsList();
        // to sort the requests list in descending order
        Collections.sort(requestItemsList);
        requestsAdapter = new RequestItemAdapter(getContext(),R.layout.request_item,requestItemsList);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        requests.setAdapter(requestsAdapter);
    }

    public static void initializeRequest(DocumentSnapshot document, Context context){
        RequestItem requestItem = new RequestItem();
        requestItem.setId("#"+document.get("rid").toString());
        requestItem.setPrice(document.get("cost").toString()+" SR.");
        ArrayList<String > skus = (ArrayList<String>) document.get("sku");
        String skuname = skus.get(0);
        SKU sku = ExpertoLoading.getSkuList().get(skuname);
        requestItem.setDevice(sku.getCompanyName()+" - "+sku.getDeviceName());
        String state = ExpertoLoading.getRequestStatesList().get((long) document.get("state"));
        requestItem.setState(state);
        Date date = ((Date)document.get("created"));
        String dateString = new SimpleDateFormat("MMM d, yyyy").format(date);
        requestItem.setDate(dateString);
        problemImagesList = new ArrayList<>();
        for(int i=0; i<skus.size(); i++){
            int resource = context.getResources().getIdentifier("mobile", "drawable", context.getPackageName());
            Drawable drawable;
            drawable = context.getResources().getDrawable(resource);
            ProblemImageItem problemImageItem = new ProblemImageItem(drawable);
            problemImagesList.add(problemImageItem);
        }
        imagesAdapter = new ProblemImageItemsAdapter(context, problemImagesList);
        requestItem.setAdapter(imagesAdapter);
        ExpertoLoading.getRequestCompletedItemsList().add(requestItem);
    }

    public static void getCompletedRequestsFromDB(FirebaseAuth mAuth, FirebaseFirestore db, final Context context) {
        final CollectionReference request = db.collection("request");
        ExpertoLoading.setRequestCompletedItemsList(new ArrayList<RequestItem>());
        if (mAuth.getCurrentUser() != null) {
            request.whereEqualTo("cid", mAuth.getCurrentUser().getUid()).whereGreaterThan("state", 3).
                    whereLessThan("state", 6).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            initializeRequest(document, context);
                        }
                    } else {
                        Log.d("new", task.getException().getMessage());
                    }

                }
            });
        }
    }
}

