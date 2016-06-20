package edu.iit.hawk.vkumar17.madprojectteam13.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.io.IO_FlushedInputStream;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Movie;
import edu.iit.hawk.vkumar17.madprojectteam13.services.Svc_HttpRetriever;


public class UI_MoviesAdapter extends ArrayAdapter<Model_Movie> {

    private Activity context;
    private ArrayList<Model_Movie> movies;

    private Svc_HttpRetriever svcHttpRetriever = new Svc_HttpRetriever();

    public UI_MoviesAdapter(Context context, int resource, ArrayList<Model_Movie> objects) {
        super(context, resource, objects);
        this.context = (Activity)context;
        movies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.movie_data_row, null);
        }


        Model_Movie modelMovie = movies.get(position);

        if(modelMovie != null){
            TextView nameTextView = (TextView)view.findViewById(R.id.name_text_view);
            nameTextView.setText(modelMovie.name);

            TextView ratingTextView = (TextView)view.findViewById(R.id.rating_text_view);
            ratingTextView.setText(modelMovie.rating);

            TextView releaseDateTextView = (TextView)view.findViewById(R.id.released_text_view);
            releaseDateTextView.setText(modelMovie.released);

            ImageView imageView = (ImageView)view.findViewById(R.id.movie_thumb_icon);
            String url = modelMovie.retrieveThumbnailUrl();

            if(url!=null) {
                Bitmap bitmap = fetchBitmapFromCache(url);
                if(bitmap == null){
                    new BitmapDownloaderTask(imageView).execute(url);
                }
                else
                    imageView.setImageBitmap(bitmap);
            }
            else
                imageView.setImageBitmap(null);
        }

        return view;
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

    private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap>{

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
