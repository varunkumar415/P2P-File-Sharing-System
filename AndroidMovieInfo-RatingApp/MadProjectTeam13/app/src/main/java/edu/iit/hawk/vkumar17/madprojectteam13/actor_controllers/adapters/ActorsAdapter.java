package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actor_activities.Actor_FilmsActivity;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actorhttp.AsyncDownloader;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actorhttp.MovieDbUrl;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.transformation.Img_RoundedTransformation;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Actor;


public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ViewHolder> {

    private static List<Model_Actor> modelActors;
    private Context context;

    public ActorsAdapter(Context ctx, List<Model_Actor> actorsList) {
        context = ctx;
        modelActors = actorsList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView actorName;
        public TextView knownFor;
        public ImageView posterImage;


        public ViewHolder(View itemView) {
            super(itemView);
            actorName = (TextView) itemView.findViewById(R.id.relatedActorsName);
            knownFor = (TextView) itemView.findViewById(R.id.knownFor);
            posterImage = (ImageView) itemView.findViewById(R.id.actorPic);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            int actorUniqueId = modelActors.get(position).getId();

            MovieDbUrl url = MovieDbUrl.getInstance();
            String getFilmographyHttpMethod = url.getFilmographyQuery(actorUniqueId);

            AsyncDownloader downloader = new AsyncDownloader(context, new Actor_FilmsActivity().getClass());
            downloader.execute(getFilmographyHttpMethod);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.actors_list_item,
                        parent,
                        false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model_Actor modelActor = modelActors.get(position);

        holder.actorName.setText(modelActor.getName());
        holder.knownFor.setText(modelActor.getKnownForFilm());
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w185" + modelActor.getPicturePath())
                .transform(new Img_RoundedTransformation(20, 5))
                .error(R.drawable.noprofile)
                .into(holder.posterImage);
    }


    @Override
    public int getItemCount() {
        if (modelActors == null) {
            return 0;
        }
        return modelActors.size();
    }


}
