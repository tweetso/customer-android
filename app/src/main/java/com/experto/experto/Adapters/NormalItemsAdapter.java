package com.experto.experto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.experto.experto.ListItems.NormaItem;
import com.experto.experto.R;

import java.util.List;

public class NormalItemsAdapter extends ArrayAdapter<NormaItem> {

    public NormalItemsAdapter(Context context, int resource, List<NormaItem> itemList) {
        super(context, resource, itemList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.normal_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.grid_title);

        NormaItem item = getItem(position);

        title.setText(item.getName());
        return convertView;
    }
}