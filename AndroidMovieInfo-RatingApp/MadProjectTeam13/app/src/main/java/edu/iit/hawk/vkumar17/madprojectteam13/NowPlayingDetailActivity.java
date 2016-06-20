package edu.iit.hawk.vkumar17.madprojectteam13;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NowPlayingDetailActivity extends Activity {
	private ImageView ivPosterImage;
	private TextView tvTitle;
	private TextView tvSynopsis;
	private TextView tvCast;
	private TextView tvAudienceScore;
	private TextView tvCriticsScore;
	//private TextView tvCriticsConsensus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_now_playing_detail);
		// Fetch views
		ivPosterImage = (ImageView) findViewById(R.id.ivPosterImage);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);
		tvCast = (TextView) findViewById(R.id.tvCast);
		//tvCriticsConsensus = (TextView) findViewById(R.id.tvCriticsConsensus);
		tvAudienceScore =  (TextView) findViewById(R.id.tvAudienceScore);
		tvCriticsScore = (TextView) findViewById(R.id.tvCriticsScore);
		// Load movie data
		NowPlayingMovie movie = (NowPlayingMovie) getIntent().getSerializableExtra(NowPlayingActivity.MOVIE_DETAIL_KEY);
		try {
			loadMovie(movie);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	// Populate the data for the movie
	@SuppressLint("NewApi")
	public void loadMovie(NowPlayingMovie movie) {
		if (android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN) {
			try {
				getActionBar().setTitle(movie.getTitle());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		// Populate data
		tvTitle.setText(Html.fromHtml("<b>"+movie.getTitle()+"</b>"));
		tvCriticsScore.setText(Html.fromHtml("<b>Critics Score:</b> " + movie.getCriticsScore() + "%"));
		tvAudienceScore.setText(Html.fromHtml("<b>Audience Score:</b> " + movie.getAudienceScore() + "%"));
		tvCast.setText(Html.fromHtml("<b>Cast:</b> "+ movie.getCastList()+"</b>"));
		tvSynopsis.setText(Html.fromHtml("<b>Movie Overview:</b> " + movie.getSynopsis()));
		//tvCriticsConsensus.setText(Html.fromHtml("<b>Consensus:</b> " + movie.getCriticsConsensus()));
		// R.drawable.large_movie_poster from 
        // http://content8.flixster.com/movie/11/15/86/11158674_pro.jpg -->
		Picasso.with(this).load(movie.getLargePosterUrl()).placeholder(R.drawable.large_movie_poster).into(ivPosterImage);
	}

}
