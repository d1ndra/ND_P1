package com.example.d1ndra.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by d1ndra on 10/31/16.
 */

public class MovieDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail, new PlaceholderFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public static class PlaceholderFragment extends Fragment {

        private String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.movie_detail, container, false);
//            rootView.setHas
            if (intent != null && intent.hasExtra("title")) {
                String title= intent.getStringExtra("title");
                String poster_path= intent.getStringExtra("poster_path");
                String release_date = intent.getStringExtra("release_date");
                String vote_average= intent.getStringExtra("vote_average");
                String overview = intent.getStringExtra("overview");

                String imgUrl = PopularMovies.baseUrl + PopularMovies.size + poster_path;
                ImageView poster = (ImageView) rootView.findViewById(R.id.image_detail);
                Picasso.with(getContext()).load(imgUrl).into(poster);
                TextView titleTV = (TextView) rootView.findViewById(R.id.title_detail);
                TextView release_dateTV = (TextView) rootView.findViewById(R.id.release_detail);
                TextView vote_averageTV = (TextView) rootView.findViewById(R.id.vote_detail);
                TextView overviewTV = (TextView) rootView.findViewById(R.id.synopsis_detail);
                titleTV.setText(title);
                release_dateTV.setText(release_date);
                vote_averageTV.setText(vote_average);
                overviewTV.setText(overview);
            }
            else {
                Log.v(LOG_TAG, "no intent data");
            }
            return rootView;
        }
    }

}
