package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linearlistview.LinearListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.iit.hawk.vkumar17.madprojectteam13.adapters.Adapter_Review;
import edu.iit.hawk.vkumar17.madprojectteam13.adapters.Adapter_Trailer;
import edu.iit.hawk.vkumar17.madprojectteam13.data.Data_MovieContract;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Moviepop;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Review;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Trailer;


public class DetailActivityFragment extends Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    private Model_Moviepop mModelMoviepop;

    private ImageView mImageView;

    private TextView mTitleView;
    private TextView mOverviewView;
    private TextView mDateView;
    private TextView mVoteAverageView;

    private LinearListView mTrailersView;
    private LinearListView mReviewsView;

    private CardView mReviewsCardview;
    private CardView mTrailersCardview;

    private Adapter_Trailer mAdapterTrailer;
    private Adapter_Review mAdapterReview;

    private ScrollView mDetailLayout;

    private Toast mToast;

    private ShareActionProvider mShareActionProvider;

    // the first trailer video to share
    private Model_Trailer mModelTrailer;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mModelMoviepop != null) {
            inflater.inflate(R.menu.menu_fragment_detail, menu);

            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            MenuItem action_share = menu.findItem(R.id.action_share);

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(), mModelMoviepop.getId());
                }

                @Override
                protected void onPostExecute(Integer isFavorited) {
                    action_favorite.setIcon(isFavorited == 1 ?
                            R.drawable.abc_btn_rating_star_on_mtrl_alpha :
                            R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();

            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            if (mModelTrailer != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                if (mModelMoviepop != null) {

                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected Integer doInBackground(Void... params) {
                            return Utility.isFavorited(getActivity(), mModelMoviepop.getId());
                        }

                        @Override
                        protected void onPostExecute(Integer isFavorited) {

                            if (isFavorited == 1) {

                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                Data_MovieContract.MovieEntry.CONTENT_URI,
                                                Data_MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                                new String[]{Integer.toString(mModelMoviepop.getId())}
                                        );
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }

                            else {

                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();

                                        values.put(Data_MovieContract.MovieEntry.COLUMN_MOVIE_ID, mModelMoviepop.getId());
                                        values.put(Data_MovieContract.MovieEntry.COLUMN_TITLE, mModelMoviepop.getTitle());
                                        values.put(Data_MovieContract.MovieEntry.COLUMN_IMAGE, mModelMoviepop.getImage());
                                        values.put(Data_MovieContract.MovieEntry.COLUMN_IMAGE2, mModelMoviepop.getImage2());
                                        values.put(Data_MovieContract.MovieEntry.COLUMN_OVERVIEW, mModelMoviepop.getOverview());
                                        values.put(Data_MovieContract.MovieEntry.COLUMN_RATING, mModelMoviepop.getRating());
                                        values.put(Data_MovieContract.MovieEntry.COLUMN_DATE, mModelMoviepop.getDate());

                                        return getActivity().getContentResolver().insert(Data_MovieContract.MovieEntry.CONTENT_URI,
                                                values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mModelMoviepop = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDetailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);

        if (mModelMoviepop != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }

        mImageView = (ImageView) rootView.findViewById(R.id.detail_image);

        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date);
        mVoteAverageView = (TextView) rootView.findViewById(R.id.detail_vote_average);

        mTrailersView = (LinearListView) rootView.findViewById(R.id.detail_trailers);
        mReviewsView = (LinearListView) rootView.findViewById(R.id.detail_reviews);

        mReviewsCardview = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);
        mTrailersCardview = (CardView) rootView.findViewById(R.id.detail_trailers_cardview);

        mAdapterTrailer = new Adapter_Trailer(getActivity(), new ArrayList<Model_Trailer>());
        mTrailersView.setAdapter(mAdapterTrailer);

        mTrailersView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                Model_Trailer modelTrailer = mAdapterTrailer.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + modelTrailer.getKey()));
                startActivity(intent);
            }
        });

        mAdapterReview = new Adapter_Review(getActivity(), new ArrayList<Model_Review>());
        mReviewsView.setAdapter(mAdapterReview);

        if (mModelMoviepop != null) {

            String image_url = Utility.buildImageUrl(342, mModelMoviepop.getImage2());

            Glide.with(this).load(image_url).into(mImageView);

            mTitleView.setText(mModelMoviepop.getTitle());
            mOverviewView.setText(mModelMoviepop.getOverview());

            String movie_date = mModelMoviepop.getDate();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = DateUtils.formatDateTime(getActivity(),
                        formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
                mDateView.setText(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mVoteAverageView.setText(Integer.toString(mModelMoviepop.getRating()));
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mModelMoviepop != null) {
            new FetchTrailersTask().execute(Integer.toString(mModelMoviepop.getId()));
            new FetchReviewsTask().execute(Integer.toString(mModelMoviepop.getId()));
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mModelMoviepop.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + mModelTrailer.getKey());
        return shareIntent;
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, List<Model_Trailer>> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        private List<Model_Trailer> getTrailersDataFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Model_Trailer> results = new ArrayList<>();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Model_Trailer modelTrailerModel = new Model_Trailer(trailer);
                    results.add(modelTrailerModel);
                }
            }

            return results;
        }

        @Override
        protected List<Model_Trailer> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
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
                return getTrailersDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<Model_Trailer> modelTrailers) {
            if (modelTrailers != null) {
                if (modelTrailers.size() > 0) {
                    mTrailersCardview.setVisibility(View.VISIBLE);
                    if (mAdapterTrailer != null) {
                        mAdapterTrailer.clear();
                        for (Model_Trailer modelTrailer : modelTrailers) {
                            mAdapterTrailer.add(modelTrailer);
                        }
                    }

                    mModelTrailer = modelTrailers.get(0);
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareMovieIntent());
                    }
                }
            }
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, List<Model_Review>> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        private List<Model_Review> getReviewsDataFromJson(String jsonStr) throws JSONException {
            JSONObject reviewJson = new JSONObject(jsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            List<Model_Review> results = new ArrayList<>();

            for(int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                results.add(new Model_Review(review));
            }

            return results;
        }

        @Override
        protected List<Model_Review> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
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
                return getReviewsDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<Model_Review> modelReviews) {
            if (modelReviews != null) {
                if (modelReviews.size() > 0) {
                    mReviewsCardview.setVisibility(View.VISIBLE);
                    if (mAdapterReview != null) {
                        mAdapterReview.clear();
                        for (Model_Review modelReview : modelReviews) {
                            mAdapterReview.add(modelReview);
                        }
                    }
                }
            }
        }
    }
}
