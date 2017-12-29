package com.udacity.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    Uri videoUri;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private int selectedIndex;
    private Handler mainHandler;
    private ListItemClickListener itemClickListener;

    public RecipeStepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView;
        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();

        itemClickListener = (RecipeDetailActivity) getActivity();
        Log.d(TAG, "1...............................................");

        if (savedInstanceState != null) {
            steps = (Steps[]) savedInstanceState.getSerializable("steps");
            recipe = (Recipes) savedInstanceState.getSerializable("recipe");
            Log.d(TAG, "4...............................................");
            selectedIndex = savedInstanceState.getInt("index");
            recipeName = savedInstanceState.getString("recipeName");
            position = savedInstanceState.getLong("position", C.TIME_UNSET);
        } else {
            steps = (Steps[]) getArguments().getSerializable("steps");
            recipe = (Recipes) getArguments().getSerializable("recipes");
            if (recipe != null) {
                Log.d(TAG, "2..............................................." + recipe.getName());
            }
            if (steps != null) {
                steps = (Steps[]) getArguments().getSerializable("steps");
                selectedIndex = getArguments().getInt("index");
                recipeName = getArguments().getString("recipeName");
            } else {
                //casting List to ArrayList
                if (recipe != null) {
                    steps = recipe.getSteps();
                }
                selectedIndex = 0;
            }

        }
        Log.d(TAG, "3...............................................");


        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        textView = rootView.findViewById(R.id.recipe_step_detail_text);
        textView.setText(steps[selectedIndex].getDescription());
        textView.setVisibility(View.VISIBLE);

        simpleExoPlayerView = rootView.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        String videoURL = steps[selectedIndex].getVideoURL();

        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
            recipeName = ((RecipeDetailActivity) getActivity()).recipeName;
            ((RecipeDetailActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        String imageUrl = steps[selectedIndex].getThumbnailURL();
        ImageView thumbImage = rootView.findViewById(R.id.thumbImage);
        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        } else {
            if (recipe.getImage() != null) {
                imageUrl = recipe.getImage();
                Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
                Picasso.with(getContext()).load(builtUri).into(thumbImage);
            }
        }

        if (!videoURL.isEmpty()) {

            videoUri = Uri.parse(steps[selectedIndex].getVideoURL());
            initializePlayer(videoUri);

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail") != null || rootView.findViewWithTag("phone-land") != null) {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                textView.setVisibility(View.GONE);
            }
        } else {
            player = null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_off_white_36dp));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }


        Button mPrevStep = rootView.findViewById(R.id.previousStep);
        Button mNextstep = rootView.findViewById(R.id.nextStep);

        mPrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Integer.parseInt(steps[selectedIndex].getId()) > 0) {
                    if (player != null) {
                        player.stop();
                    }
                    itemClickListener.onListItemClick(steps, Integer.parseInt(steps[selectedIndex].getId()) - 1, recipeName);
                } else {
                    Toast.makeText(getActivity(), "You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int lastIndex = steps.length - 1;
                if (Integer.parseInt(steps[selectedIndex].getId()) < Integer.parseInt(steps[lastIndex].getId())) {
                    if (player != null) {
                        player.stop();
                    }
                    itemClickListener.onListItemClick(steps, Integer.parseInt(steps[selectedIndex].getId()) + 1, recipeName);
                } else {
                    Toast.makeText(getContext(), "You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }
        });



        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            if (position != C.TIME_UNSET) {
                player.seekTo(position);
            }
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable("steps", steps);
        currentState.putSerializable("recipe", recipe);
        currentState.putInt("index", selectedIndex);
        currentState.putString("recipeName", recipeName);
        currentState.putLong("position", position);
    }

    public boolean isInLandscapeMode(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            position = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUri != null)
            initializePlayer(videoUri);
    }

    public interface ListItemClickListener {
        void onListItemClick(Steps[] allSteps, int Index, String recipeName);
    }
}
