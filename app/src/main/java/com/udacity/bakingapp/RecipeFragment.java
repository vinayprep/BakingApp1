
package com.udacity.bakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.adapters.RecipeAdapter;
import com.udacity.bakingapp.model.RestClient;
import com.udacity.bakingapp.pojo.Recipes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeFragment extends Fragment {

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    ArrayList<Recipes> recipes = new ArrayList<Recipes>();
    private List<Recipes> mModels;
    private RecipeAdapter mAdapter;
    private RecyclerView recyclerView;


    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_fragment, container, false);

        mAdapter = new RecipeAdapter((MainActivity) getActivity());
        recyclerView = rootView.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.setAdapter(mAdapter);
        mModels = new ArrayList<>();

        if (rootView.getTag() != null && rootView.getTag().equals("tab")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        getReceipes();

        return rootView;
    }

    void getReceipes() {
        final Context context = getContext();

        if (ConnectionDetector.isConnectedToInternet()) {
            Call<ArrayList<Recipes>> call = RestClient.post().getReceipes();
            call.enqueue(new Callback<ArrayList<Recipes>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipes>> call, Response<ArrayList<Recipes>> response) {
                    if (response.isSuccessful()) {
                        recipes = response.body();
                        if (response.body() != null) {
                            if (response.body() == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("No Recipes Found");
                                builder.setMessage("Please check the request again.");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            } else {
                                publish();
                            }
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Server Not Responding");
                        builder.setMessage("Server Not Responding. Please try again later");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Recipes>> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Server Not Responding");
                    builder.setMessage("Server Not Responding. Please try again later");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No Internet Connection.");
            builder.setMessage("No Internet Connection. Please Try Again.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    private void publish() {
        mModels.clear();
        mModels.addAll(recipes);
        mAdapter.replaceAll(mModels);
        recyclerView.scrollToPosition(0);
    }


}
