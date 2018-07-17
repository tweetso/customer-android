package com.experto.experto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.experto.experto.ListItems.ProblemPriceItem;
import com.experto.experto.R;

import java.util.List;


public class ProblemPriceItemsAdapter extends ArrayAdapter<ProblemPriceItem> {

    public ProblemPriceItemsAdapter(Context context, int resource, List<ProblemPriceItem> itemList) {
        super(context, resource, itemList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.problem_price_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.problem_name);
        TextView price = (TextView) convertView.findViewById(R.id.problem_price);

        ProblemPriceItem item = getItem(position);

        name.setText(item.getName());
        price.setText(item.getPrice());
        return convertView;
    }
}
