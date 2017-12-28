package com.udacity.bakingapp.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.bakingapp.MainActivity;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.pojo.Recipes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rats on 14/6/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.BrandViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private List<Recipes> mSortedList = new ArrayList<Recipes>();

    private AppCompatActivity context;

    public RecipeAdapter(AppCompatActivity context) {
        this.context = context;
    }

    public void add(Recipes model) {
        mSortedList.add(model);
    }

    public void remove(Recipes model) {
        mSortedList.remove(model);
    }

    public void add(List<Recipes> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<Recipes> models) {
        for (Recipes model : models) {
            mSortedList.remove(model);
        }
    }

    public void replaceAll(List<Recipes> models) {
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final Recipes model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_master, parent, false);
        return new BrandViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        String recipes = mSortedList.get(position).getName();
        holder.recipeName.setText(recipes);
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;
        LinearLayout recipePage;

        BrandViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipePage = itemView.findViewById(R.id.recipe_page);
            recipePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).onClick(mSortedList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
