package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actorhttp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class AsyncDownloader extends AsyncTask<String, Integer, String> {

    public static final String TAG = AsyncDownloader.class.getSimpleName();

    private Context context;
    private Class classToLoad;
    private ProgressDialog dialog;

    private String url;

    public AsyncDownloader(Context ctx, Class c) {
        context = ctx;
        classToLoad = c;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(dialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);

        Response response = null;

        String jsonData = null;

        try {
            response = call.execute();

            if (response.isSuccessful()) {
                jsonData = response.body().string();

            } else {
                jsonData = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData; //This is returned to onPostExecute()
    }

    /**
     * onPostExecute runs on the  main (GUI) thread and receives
     * the result of doInBackground.
     *
     * Here we pass a string representation of jsonData to the child/receiver
     * activity.
     *
     * @param jsonData
     */
    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        dialog.dismiss();

        Intent i = new Intent(context, classToLoad);
        i.putExtra("jsonData", jsonData);
        context.startActivity(i);
    }
}