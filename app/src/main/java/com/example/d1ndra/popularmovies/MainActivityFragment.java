package com.example.d1ndra.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by d1ndra on 10/30/16.
 */

public class MainActivityFragment extends Fragment {
    PopularMoviesAdapter popularMoviesAdapter;

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMovieTask fmt = new FetchMovieTask();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String sort_by = sharedPref.getString(getString(R.string.pref_gen_sort_key),
                getString(R.string.pref_gen_sort_default));
        fmt.execute(sort_by);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        popularMoviesAdapter = new PopularMoviesAdapter(getActivity(),
                new ArrayList<PopularMovies>());
        GridView gv = (GridView) rootView.findViewById(R.id.movies_grid);
        gv.setAdapter(popularMoviesAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopularMovies movie = popularMoviesAdapter.getItem((int) id);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class)
                        .putExtra("title", movie.title)
                        .putExtra("poster_path", movie.poster_path)
                        .putExtra("vote_average", movie.vote_average)
                        .putExtra("synopsis", movie.overview)
                        .putExtra("release_date", movie.release_date);
                startActivity(intent);

            }
        });
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, JSONArray> {
        public FetchMovieTask() {

        }

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected void onPostExecute(JSONArray movielist) {
            if (movielist != null) {
                Log.v(LOG_TAG, "successfully fetched");
                popularMoviesAdapter.clear();
                ArrayList<PopularMovies> pmList = new ArrayList<>();
                for(int i = 0; i<movielist.length(); i++) {
                    try {
                        JSONObject tempObj = movielist.getJSONObject(i);
                        pmList.add(new PopularMovies(tempObj));

                    } catch (JSONException je) {
                        Log.v(LOG_TAG, je.toString());
                    }
                }
                popularMoviesAdapter.addAll(pmList);
            }
        }

            protected JSONArray doInBackground(String... params) {
                if(params.length == 0)
                    return null;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String response = null;
                JSONArray movielist = null;
                try {
                    final String SORT_PARAM = "sort_by";
                    final String API_KEY_PARAM = "api_key";
                    Uri.Builder builder = Uri.parse(Constants.BASE_API_URL).buildUpon();
                    builder.appendQueryParameter(SORT_PARAM, params[0]);
                    builder.appendQueryParameter(API_KEY_PARAM, BuildConfig.tmdb_api_key_v3);
                    String completeURL = builder.build().toString();
                    URL url = new URL(completeURL);
                    Log.v(LOG_TAG, "URL" + " " + completeURL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inp = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if(inp == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inp));
                    String line;
                    while((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                    }
                    if(buffer.length() == 0) {
                        return null;
                    }
                    response = buffer.toString();
                    JSONObject resJSON = new JSONObject(response);
                    movielist = resJSON.getJSONArray("results");

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    return null;
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    return null;
                } finally {
                    if(urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if(reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                return movielist;
        }
    }
}
