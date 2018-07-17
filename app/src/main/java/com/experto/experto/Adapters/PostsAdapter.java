package com.experto.experto.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.experto.experto.AppData.Post;
import com.experto.experto.R;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends ArrayAdapter<Post> {

    private DateTime currentDate = new DateTime(new Date());

    public PostsAdapter(Context context, int resource, List<Post> itemList) {
        super(context, resource, itemList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.post_item, parent, false);
        }

        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView time = (TextView) convertView.findViewById(R.id.time_text);
        ImageView image = (ImageView) convertView.findViewById(R.id.post_image);
        Post item = getItem(position);

        content.setText(item.getContent());

        Date postDate = new Date(item.getTimeStamp().getSeconds()*1000);
        DateTime postTime= new DateTime(postDate);
        int weeks = Weeks.weeksBetween(postTime, currentDate).getWeeks();
        time.setText(weeks+" weeks ago");
        String imageUrl = item.getImageURL();
        Glide.with(super.getContext()).load(imageUrl).into(image);
        return convertView;
    }
}

