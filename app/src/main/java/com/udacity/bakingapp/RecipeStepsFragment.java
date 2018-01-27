package com.udacity.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.pojo.Recipes;
import com.udacity.bakingapp.pojo.Steps;

public class RecipeStepsFragment extends Fragment {
    private static final String TAG = RecipeStepsFragment.class.getSimpleName();
    Steps[] steps;
    Recipes recipe;
    String recipeName;
    long position = C.TIME_UNSET;
    Uri uri;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private Handler handler;
    private BandwidthMeter bandwidthMeter;
    private int index;
    private ListItemClickListener listItemClickListener;

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatTextView textView;
        listItemClickListener = (RecipeDetailActivity) getActivity();

        if (savedInstanceState != null) {
            steps = (Steps[]) savedInstanceState.getSerializable("steps");
            recipe = (Recipes) savedInstanceState.getSerializable("recipe");
            index = savedInstanceState.getInt("index");
            recipeName = savedInstanceState.getString("recipeName");
            position = savedInstanceState.getLong("position", C.TIME_UNSET);
        } else {
            steps = (Steps[]) getArguments().getSerializable("steps");
            recipe = (Recipes) getArguments().getSerializable("recipes");
            if (steps != null) {
                steps = (Steps[]) getArguments().getSerializable("steps");
                index = getArguments().getInt("index");
                recipeName = getArguments().getString("recipeName");
            } else {
                if (recipe != null) {
                    steps = recipe.getSteps();
                }
                index = 0;
            }

        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        textView = rootView.findViewById(R.id.recipe_steps);
        textView.setVisibility(View.VISIBLE);
        textView.setText(steps[index].getDescription());

        handler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        simpleExoPlayerView = rootView.findViewById(R.id.player_view);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        if (rootView.findViewWithTag("sw600dp-port-steps") != null) {
            recipeName = ((RecipeDetailActivity) getActivity()).recipeName;
            ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        String image = steps[index].getThumbnailURL();
        AppCompatImageView imageView = rootView.findViewById(R.id.image_view);
        if (image != "") {
            Uri builtUri = Uri.parse(image).buildUpon().build();
            Picasso.with(getContext()).load(builtUri).into(imageView);
        } else {
            if (recipe.getImage() != null) {
                image = recipe.getImage();
                Uri builtUri = Uri.parse(image).buildUpon().build();
                Picasso.with(getContext()).load(builtUri).into(imageView);
            }
        }

        String videoURL = steps[index].getVideoURL();

        if (!videoURL.isEmpty()) {

            uri = Uri.parse(steps[index].getVideoURL());
            initializePlayer(uri);

            if (rootView.findViewWithTag("sw600dp-land-steps") != null) {
                getActivity().findViewById(R.id.recipe_steps).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                textView.setVisibility(View.GONE);
            }
        } else {
            simpleExoPlayer = null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }


        AppCompatButton previousStepBtn = rootView.findViewById(R.id.previous_step);
        AppCompatButton nextStepBtn = rootView.findViewById(R.id.next_step);

        previousStepBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Integer.parseInt(steps[index].getId()) > 0) {
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.stop();
                    }
                    listItemClickListener.onListItemClick(steps, Integer.parseInt(steps[index].getId()) - 1, recipeName);
                } else {
                    Toast.makeText(getActivity(), "You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int lastIndex = steps.length - 1;
                if (Integer.parseInt(steps[index].getId()) < Integer.parseInt(steps[lastIndex].getId())) {
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.stop();
                    }
                    listItemClickListener.onListItemClick(steps, Integer.parseInt(steps[index].getId()) + 1, recipeName);
                } else {
                    Toast.makeText(getContext(), "You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(handler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            if (position != C.TIME_UNSET) {
                simpleExoPlayer.seekTo(position);
            }
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable("steps", steps);
        currentState.putSerializable("recipe", recipe);
        currentState.putInt("index", index);
        currentState.putString("recipeName", recipeName);
        currentState.putLong("position", position);
    }

    public boolean isInLandscapeMode(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            position = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (uri != null)
            initializePlayer(uri);
    }

    public interface ListItemClickListener {
        void onListItemClick(Steps[] allSteps, int Index, String recipeName);
    }
}
