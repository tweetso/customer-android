package com.experto.experto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.experto.experto.ListItems.RequestItem;
import com.experto.experto.R;

import java.util.List;


public class RequestItemAdapter extends ArrayAdapter<RequestItem> {
    Context context;
    public RequestItemAdapter(Context context, int resource, List<RequestItem> itemList) {
        super(context, resource, itemList);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.request_item, parent, false);
        }

        TextView device = (TextView) convertView.findViewById(R.id.device_name);
        TextView price = (TextView) convertView.findViewById(R.id.request_price);
        final TextView id = (TextView) convertView.findViewById(R.id.request_id);
        TextView state = (TextView) convertView.findViewById(R.id.request_state);
        TextView date = (TextView) convertView.findViewById(R.id.request_date);
        HorizontalGridView problemsImages = (HorizontalGridView) convertView.findViewById(R.id.problem_images_grid);
        Button cancelRequest = (Button) convertView.findViewById(R.id.cancel_request);
        RequestItem item = getItem(position);
        device.setText(item.getDevice());
        price.setText(item.getPrice());
        id.setText(item.getId());
        state.setText(item.getState());
        date.setText(item.getDate());
        problemsImages.setAdapter(item.getAdapter());
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Request Id number"+id.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }
}
