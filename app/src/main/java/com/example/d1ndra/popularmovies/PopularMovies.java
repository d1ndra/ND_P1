package com.example.d1ndra.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by d1ndra on 10/31/16.
 */

public class PopularMovies {
    public static String baseUrl = "https://image.tmdb.org/t/p/";
    public static String size = "w342";
    public static String size_detail = "w500";
    static public String LOG_TAG = PopularMovies.class.getSimpleName();
    public String poster_path;
    public String release_date;
    public String title;
    public String vote_average;
    public String overview;

    public PopularMovies(JSONObject jobj) {
        try {
            this.poster_path = jobj.getString("poster_path");
            this.release_date = jobj.getString("release_date");
            this.title = jobj.getString("title");
            this.vote_average = jobj.getString("vote_average");
            this.overview = jobj.getString("overview");
        }
        catch (JSONException je) {
            Log.v(LOG_TAG, je.toString());
        }

    }
}
