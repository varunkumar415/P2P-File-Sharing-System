package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.transformation.Img_RoundedTransformation;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Actor_Film;

public class ActorFilmsAdapter extends RecyclerView.Adapter<ActorFilmsAdapter.ViewHolder> {

    private List<Model_Actor_Film> actorFilms;
    private Context context;

    public ActorFilmsAdapter(Context ctx, List<Model_Actor_Film> filmsArray) {
        context = ctx;
        actorFilms = filmsArray;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView filmTitle;
        public ImageView posterImage;
        public TextView dateValue;
        public TextView roleValue;

        public ViewHolder(View itemView) {
            super(itemView);
            filmTitle = (TextView) itemView.findViewById(R.id.filmTitleLabel);
            posterImage = (ImageView) itemView.findViewById(R.id.filmPosterImage);
            dateValue = (TextView) itemView.findViewById(R.id.dateValue);
            roleValue = (TextView) itemView.findViewById(R.id.roleLabel);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.actorfilms_list_item,
                        parent,
                        false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model_Actor_Film actorFilm = actorFilms.get(position);

        holder.filmTitle.setText(actorFilm.getTitle());
        holder.dateValue.setText(actorFilm.getFormattedDate());
        holder.roleValue.setText(actorFilm.getRole());
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w185" + actorFilm.getPosterPath())
                .transform(new Img_RoundedTransformation(20, 5))
                .error(R.drawable.noprofile)
                .into(holder.posterImage);


        Typeface latoBlack = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Black.ttf");
        holder.filmTitle.setTypeface(latoBlack);
    }


    @Override
    public int getItemCount() {
        if (actorFilms == null) {
            return 0;
        }
        return actorFilms.size();
    }


}
