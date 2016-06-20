package edu.iit.hawk.vkumar17.madprojectteam13.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Moviepop;

public class Adapter_MovieGrid extends BaseAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final Model_Moviepop mLock = new Model_Moviepop();

    private List<Model_Moviepop> mObjects;

    public Adapter_MovieGrid(Context context, List<Model_Moviepop> objects) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mObjects = objects;
    }

    public Context getContext() {
        return mContext;
    }

    public void add(Model_Moviepop object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    public void setData(List<Model_Moviepop> data) {
        clear();
        for (Model_Moviepop modelMoviepop : data) {
            add(modelMoviepop);
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Model_Moviepop getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.grid_item_movie, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final Model_Moviepop modelMoviepop = getItem(position);

        String image_url = "http://image.tmdb.org/t/p/w185" + modelMoviepop.getImage();

        viewHolder = (ViewHolder) view.getTag();

        Glide.with(getContext()).load(image_url).into(viewHolder.imageView);
        viewHolder.titleView.setText(modelMoviepop.getTitle());

        return view;
    }

    public static class ViewHolder {
        public final ImageView imageView;
        public final TextView titleView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            titleView = (TextView) view.findViewById(R.id.grid_item_title);
        }
    }
}
