package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NowPlayingMoviesAdapter extends ArrayAdapter<NowPlayingMovie> {
	public NowPlayingMoviesAdapter(Context context, ArrayList<NowPlayingMovie> aMovies) {
		super(context, 0, aMovies);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		NowPlayingMovie movie = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.adapter_item_now_playing_movie, null);
		}
		// Lookup view for data population
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		TextView tvCriticsScore = (TextView) convertView.findViewById(R.id.tvCriticsScore);
		TextView tvCast = (TextView) convertView.findViewById(R.id.tvCast);
		ImageView ivPosterImage = (ImageView) convertView.findViewById(R.id.ivPosterImage);
		// Populate the data into the template view using the data object
		tvTitle.setText(Html.fromHtml("<b>"+ movie.getTitle()+"</b>"));
		tvCriticsScore.setText(Html.fromHtml("<b>Score: " + movie.getCriticsScore() + "% </b>"));
		tvCast.setText(Html.fromHtml("<b>Cast:</b> "+movie.getCastList()+"</b>"));
		Picasso.with(getContext()).load(movie.getPosterUrl()).into(ivPosterImage);
		// Return the completed view to render on screen
		return convertView;
	}
}
