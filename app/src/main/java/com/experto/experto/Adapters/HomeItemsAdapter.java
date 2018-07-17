package com.experto.experto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.experto.experto.ListItems.HomeItem;
import com.experto.experto.R;

import java.util.List;

public class HomeItemsAdapter extends ArrayAdapter<HomeItem> {

    public HomeItemsAdapter(Context context, int resource, List<HomeItem> itemList) {
        super(context, resource, itemList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.home_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.grid_img);
        TextView title = (TextView) convertView.findViewById(R.id.grid_title);

        HomeItem grid = getItem(position);

        title.setText(grid.getName());
        photoImageView.setImageDrawable(grid.getImg());
        return convertView;
    }
}