package edu.iit.hawk.vkumar17.madprojectteam13.ui;

import android.app.Dialog;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.io.IO_FlushedInputStream;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Movie;
import edu.iit.hawk.vkumar17.madprojectteam13.services.Svc_HttpRetriever;

public class MoviesListActivity extends ListActivity {

    private ArrayList<Model_Movie> moviesList;
    private UI_MoviesAdapter UIMoviesAdapter;


    private Svc_HttpRetriever svcHttpRetriever = new Svc_HttpRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movies_layout);
        UIMoviesAdapter = new UI_MoviesAdapter(MoviesListActivity.this, R.layout.movie_data_row, new ArrayList<Model_Movie>());
        moviesList = (ArrayList<Model_Movie>)getIntent().getSerializableExtra("movies");
        Log.d("[varun2]",moviesList.toString());


        setListAdapter(UIMoviesAdapter);


        if(moviesList != null && !moviesList.isEmpty())
        {
            UIMoviesAdapter.notifyDataSetChanged();
            UIMoviesAdapter.clear();
            for(int i = 0; i < moviesList.size(); i++)
            {
                UIMoviesAdapter.add(moviesList.get(i));
                Log.d("[varun3-name]", moviesList.get(i).name.toString());
                Log.d("[varun3-id]", moviesList.get(i).id.toString());
               // Log.d("[varun3-imdbId]", moviesList.get(i).imdbId.toString());
                //Log.d("[varun3-language]", moviesList.get(i).language.toString());
               // Log.d("[varun3-overview]", moviesList.get(i).overview.toString());
                Log.d("[varun3-popularity]", moviesList.get(i).popularity.toString());
                Log.d("[varun3-released]", moviesList.get(i).released.toString());
                //Log.d("[varun3-score]", moviesList.get(i).score.toString());
                //Log.d("[varun3-type]", moviesList.get(i).type.toString());
                Log.d("[varun3-votes]", moviesList.get(i).votes.toString());
                //Log.d("[varun3-url]", moviesList.get(i).url.toString());

            }
        }

        UIMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Model_Movie modelMovie = UIMoviesAdapter.getItem(position);
        showMoviePosterDialog(modelMovie);
    }

    private void showMoviePosterDialog(Model_Movie modelMovie) {
        final Dialog posterDialog = new Dialog(this);
        posterDialog.setContentView(R.layout.movie_poster_dialog);
        posterDialog.setTitle(modelMovie.name);

        ImageView posterImageView = (ImageView)posterDialog.findViewById(R.id.movie_poster);
        Button closeButton = (Button)posterDialog.findViewById(R.id.close_button);

        Bitmap posterImage;
        String posterUrl = modelMovie.retrievePosterUrl();

        if(posterUrl !=null) {
            posterImage = fetchBitmapFromCache(posterUrl);
            if(posterImage == null){
                new BitmapDownloaderTask(posterImageView).execute(posterUrl);
            }
            else
                posterImageView.setImageBitmap(posterImage);
        }


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posterDialog.dismiss();
            }
        });

        posterDialog.show();
    }

    private LinkedHashMap<String, Bitmap> bitmapCache = new LinkedHashMap<String, Bitmap>();

    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (bitmapCache) {
                bitmapCache.put(url, bitmap);
            }
        }
    }

    private Bitmap fetchBitmapFromCache(String url){
        synchronized (bitmapCache) {
            final Bitmap bitmap = bitmapCache.get(url);
            if (bitmap != null) {
                // Bitmap found in cache
                // Move element to first position, so that it is removed last
                bitmapCache.remove(url);
                bitmapCache.put(url, bitmap);
                return bitmap;
            }
        }
        return null;
    }

    private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView){
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            url = strings[0];
            InputStream inputStream = svcHttpRetriever.retrieveStream(url);
            if(inputStream == null)
                return null;
            return BitmapFactory.decodeStream(new IO_FlushedInputStream(inputStream));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(isCancelled())
                bitmap = null;
            addBitmapToCache(url, bitmap);
            if(imageViewReference != null){
                ImageView imageView = imageViewReference.get();
                if(imageView != null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
