package ru.lantimat.photogallery.ui.category;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.models.Collection;
import ru.lantimat.photogallery.utils.GlideApp;
import ru.lantimat.photogallery.utils.SquareImageView;

/**
 * Created by GabdrakhmanovII on 03.11.2017.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final static String TAG = "CollectionAdapter";

    private ArrayList<Collection> mList;
    private Context context;


    public CategoryRecyclerAdapter(Context context, ArrayList<Collection> itemList) {
        this.context = context;
        this.mList = itemList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_collection, parent, false);
        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder)holder).textView.setText(mList.get(position).getTitle());

        try {

            RequestBuilder<Drawable> thumbnailRequest = GlideApp.with(context)
                    .load(mList.get(position).getCoverPhoto().getUrls().getThumb())
                    .centerCrop()
                    .override(200, 200);

            GlideApp
                    .with(context)
                    .load(mList.get(position).getCoverPhoto().getUrls().getRegular())
                    .thumbnail(thumbnailRequest)
                    .centerCrop()
                    .override(400, 400)
                    //.placeholder(R.color.colorPlaceholder)
                    .into(((ViewHolder) holder).imageView);
        } catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }

    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private SquareImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
