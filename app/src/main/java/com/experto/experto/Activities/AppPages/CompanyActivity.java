package com.experto.experto.Activities.AppPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.experto.experto.AppData.Company;
import com.experto.experto.ListItems.NormaItem;
import com.experto.experto.Adapters.NormalItemsAdapter;
import com.experto.experto.R;
import com.experto.experto.AppData.ServiceA;

import java.util.ArrayList;
import java.util.List;

public class CompanyActivity extends AppCompatActivity {

    private NormalItemsAdapter adapter;
    private List<NormaItem> companyItems;
    private GridView companyItemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_item_list);
        companyItemsList = (GridView) findViewById(R.id.normal_grid);
        companyItems = new ArrayList<NormaItem>();
        adapter = new NormalItemsAdapter(this, R.layout.normal_item, companyItems);
        final List<Company> companyList = ((ServiceA) Home.getRequestInfo().get("service")).getCompanies();
        if (companyList != null) {
            for (int i = 0; i < companyList.size(); i++) {
                Company company = companyList.get(i);
                String name = company.getName();
                NormaItem normaItem = new NormaItem(name);
                adapter.add(normaItem);

            }
            companyItemsList.setAdapter(adapter);
            companyItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // save the selected company object in the Request info list
                    Home.getRequestInfo().put("company",companyList.get(i));
                    Intent intent = new Intent(CompanyActivity.this,DeviceActivity.class);
                    startActivity(intent);

                }
            });
        }
        else {
            Toast.makeText(this,R.string.noData,Toast.LENGTH_LONG).show();
        }
    }
}
