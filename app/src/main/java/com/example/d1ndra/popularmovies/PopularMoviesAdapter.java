package com.example.d1ndra.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by d1ndra on 10/31/16.
 */

public class PopularMoviesAdapter extends ArrayAdapter<PopularMovies> {

    private static final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();
    Activity curr_context;
    public PopularMoviesAdapter(Activity context, List<PopularMovies> popularMovies) {
        super(context, 0, popularMovies);
        this.curr_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularMovies popularMovie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        ImageView posterImage = (ImageView) convertView.findViewById(R.id.movies_grid_image);
        String imgUrl = PopularMovies.baseUrl + PopularMovies.size + popularMovie.poster_path;
        Picasso.with(curr_context).load(imgUrl).into(posterImage);

//        TextView tv = (TextView) convertView.findViewById(R.id.movies_grid_text);
//        tv.setText(popularMovie.title);
        return convertView;
    }

}
