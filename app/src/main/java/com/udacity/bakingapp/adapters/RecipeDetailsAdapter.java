package com.udacity.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.pojo.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rats on 14/6/2017.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.BrandViewHolder> {
    private static final String TAG = RecipeDetailsAdapter.class.getSimpleName();
    final private ListItemClickListener lOnClickListener;
    Steps[] stepsArray;
    private List<Steps> steps = new ArrayList<Steps>();
    private String recipeName;

    public RecipeDetailsAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }

    public void add(Steps model) {
        steps.add(model);
    }

    public void remove(Steps model) {
        steps.remove(model);
    }

    public void add(List<Steps> models) {
        steps.addAll(models);
    }

    public void remove(List<Steps> models) {
        for (Steps model : models) {
            steps.remove(model);
        }
    }

    public void replaceAll(List<Steps> models, String recipeName) {
        for (int i = steps.size() - 1; i >= 0; i--) {
            final Steps model = steps.get(i);
            if (!models.contains(model)) {
                steps.remove(model);
            }
        }
        steps.addAll(models);
        stepsArray = steps.toArray(new Steps[0]);
        this.recipeName = recipeName;
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_cardview_items, parent, false);
        return new BrandViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        String shortDesc = steps.get(position).getId() + ". " + steps.get(position).getShortDescription();
        holder.shortDesc.setText(shortDesc);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(Steps[] stepsOut, int clickedItemIndex, String recipeName);
    }

    class BrandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shortDesc;

        BrandViewHolder(View itemView) {
            super(itemView);
            shortDesc = itemView.findViewById(R.id.shortDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            lOnClickListener.onListItemClick(stepsArray, getAdapterPosition(), recipeName);
        }
    }
}
