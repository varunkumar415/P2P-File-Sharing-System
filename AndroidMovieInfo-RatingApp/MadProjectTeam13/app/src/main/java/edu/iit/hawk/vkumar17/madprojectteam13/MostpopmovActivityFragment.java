package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.List;

import edu.iit.hawk.vkumar17.madprojectteam13.adapters.Adapter_MovieGrid;
import edu.iit.hawk.vkumar17.madprojectteam13.data.Data_MovieContract;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Moviepop;


public class MostpopmovActivityFragment extends Fragment {

    private GridView mGridView;

    private Adapter_MovieGrid mAdapterMovieGrid;

    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String FAVORITE = "favorite";
    private static final String MOVIES_KEY = "movies";

    private String mSortBy = POPULARITY_DESC;

    private ArrayList<Model_Moviepop> mMovies = null;

    private static final String[] MOVIE_COLUMNS = {
            Data_MovieContract.MovieEntry._ID,
            Data_MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            Data_MovieContract.MovieEntry.COLUMN_TITLE,
            Data_MovieContract.MovieEntry.COLUMN_IMAGE,
            Data_MovieContract.MovieEntry.COLUMN_IMAGE2,
            Data_MovieContract.MovieEntry.COLUMN_OVERVIEW,
            Data_MovieContract.MovieEntry.COLUMN_RATING,
            Data_MovieContract.MovieEntry.COLUMN_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_IMAGE2 = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;
    public static final int COL_DATE = 7;

    public MostpopmovActivityFragment() {
    }


    public interface Callback {
        void onItemSelected(Model_Moviepop modelMoviepop);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem action_sort_by_favorite = menu.findItem(R.id.action_sort_by_favorite);

        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (mSortBy.contentEquals(RATING_DESC)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        } else if (mSortBy.contentEquals(FAVORITE)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_favorite.setChecked(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = POPULARITY_DESC; //1
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = RATING_DESC;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_favorite: //2
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                mSortBy = FAVORITE; //3
                updateMovies(mSortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);

        mAdapterMovieGrid = new Adapter_MovieGrid(getActivity(), new ArrayList<Model_Moviepop>());

        mGridView.setAdapter(mAdapterMovieGrid);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model_Moviepop modelMoviepop = mAdapterMovieGrid.getItem(position);
                ((Callback) getActivity()).onItemSelected(modelMoviepop);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                mSortBy = savedInstanceState.getString(SORT_SETTING_KEY);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mAdapterMovieGrid.setData(mMovies);
            } else {
                updateMovies(mSortBy);
            }
        } else {
            updateMovies(mSortBy);
        }

        return view;
    }

    private void updateMovies(String sort_by) {
        if (!sort_by.contentEquals(FAVORITE)) {
            new FetchMoviesTask().execute(sort_by);
        } else {
            new FetchFavoriteMoviesTask(getActivity()).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!mSortBy.contentEquals(POPULARITY_DESC)) {
            outState.putString(SORT_SETTING_KEY, mSortBy);
        }
        if (mMovies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        }
        super.onSaveInstanceState(outState);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Model_Moviepop>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private List<Model_Moviepop> getMoviesDataFromJson(String jsonStr) throws JSONException {
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            List<Model_Moviepop> results = new ArrayList<>();

            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Model_Moviepop modelMoviepopModel = new Model_Moviepop(movie);
                results.add(modelMoviepopModel);
            }

            return results;
        }


        // check here some issue -------------------------------->>
        @Override
        protected List<Model_Moviepop> doInBackground(String... params) {
            Log.d("VARUN","=================doInBackground======>");
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.tmdb_api_key))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Model_Moviepop> movies) {
            if (movies != null) {
                if (mAdapterMovieGrid != null) {
                    mAdapterMovieGrid.setData(movies);
                }
                mMovies = new ArrayList<>();
                mMovies.addAll(movies);
            }
        }
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Model_Moviepop>> {

        private Context mContext;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        private List<Model_Moviepop> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<Model_Moviepop> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Model_Moviepop modelMoviepop = new Model_Moviepop(cursor);
                    results.add(modelMoviepop);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected List<Model_Moviepop> doInBackground(Void... params) {
            Log.d("VARUN","=================doInBackground======>");
            Cursor cursor = mContext.getContentResolver().query(
                    Data_MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoriteMoviesDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<Model_Moviepop> movies) {
            if (movies != null) {
                if (mAdapterMovieGrid != null) {
                    mAdapterMovieGrid.setData(movies);
                }
                mMovies = new ArrayList<>();
                mMovies.addAll(movies);
            }
        }
    }
}
