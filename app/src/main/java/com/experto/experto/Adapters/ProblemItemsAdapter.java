package com.experto.experto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.experto.experto.ListItems.ProblemItem;
import com.experto.experto.R;

import java.util.ArrayList;
import java.util.List;


public class ProblemItemsAdapter extends ArrayAdapter<ProblemItem> {
    private ArrayList<Integer> problemsIndexes = new ArrayList<>();

    public ProblemItemsAdapter(Context context, int resource, List<ProblemItem> itemList) {
        super(context, resource, itemList);
    }

    public ArrayList<Integer> getProblemsIndexes() {
        return problemsIndexes;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null||  convertView.getTag() == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.problem_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.problem_title);
        TextView price = (TextView) convertView.findViewById(R.id.problem_price);
        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.problem_image);
        CardView cardView = (CardView) convertView.findViewById(R.id.problem_container);
        ProblemItem item = getItem(position);
        if (problemsIndexes.contains((Integer)position)) {
            cardView.setAlpha((float)0.5);
        }
        name.setText(item.getName());
        price.setText(item.getPrice());
        photoImageView.setImageDrawable(item.getImage());
        return convertView;
    }
}
