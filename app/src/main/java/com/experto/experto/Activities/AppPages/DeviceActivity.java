package com.experto.experto.Activities.AppPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.experto.experto.AppData.Company;
import com.experto.experto.AppData.Device;
import com.experto.experto.ListItems.NormaItem;
import com.experto.experto.Adapters.NormalItemsAdapter;
import com.experto.experto.R;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends AppCompatActivity {

    private NormalItemsAdapter adapter;
    private List<NormaItem> deviceItems;
    private GridView deviceItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_item_list);
        deviceItemsList = (GridView) findViewById(R.id.normal_grid);
        deviceItems = new ArrayList<NormaItem>();
        adapter = new NormalItemsAdapter(this, R.layout.normal_item, deviceItems);
        final List<Device> deviceList = ((Company) Home.getRequestInfo().get("company")).getDevices();
        if (deviceList != null) {
            for (int i = 0; i < deviceList.size(); i++) {
                Device device = deviceList.get(i);
                String name = device.getName();
                NormaItem normaItem = new NormaItem(name);
                adapter.add(normaItem);

            }
            deviceItemsList.setAdapter(adapter);
            deviceItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // save the selected device object in the Request info list
                    Home.getRequestInfo().put("device",deviceList.get(i));
                    Intent intent = new Intent(DeviceActivity.this,ProblemActivity.class);
                    startActivity(intent);

                }
            });
        }
        else {
            Toast.makeText(this,R.string.noData,Toast.LENGTH_LONG).show();
        }
    }
    }

