package com.experto.experto.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.experto.experto.ListItems.ProblemImageItem;
import com.experto.experto.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProblemImageItemsAdapter extends RecyclerView.Adapter<ProblemImageItemsAdapter.ViewHolder> {
    private List<ProblemImageItem> problemImageItems;
    private Context context;

    public ProblemImageItemsAdapter(Context context, List<ProblemImageItem> problemImageItems){
        this.context = context;
        this.problemImageItems = problemImageItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_image_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProblemImageItem  problemImageItem = problemImageItems.get(position);
        holder.photoImageView.setImageDrawable(problemImageItem.getImg());

    }

    @Override
    public int getItemCount() {
        return problemImageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView photoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            photoImageView = (CircleImageView)itemView.findViewById(R.id.problem_image);
        }
    }
}


