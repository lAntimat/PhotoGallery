package ru.lantimat.photogallery.browse;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.models.Photo;
import ru.lantimat.photogallery.utils.GlideApp;
import ru.lantimat.photogallery.utils.SquareImageView;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

/**
 * Created by GabdrakhmanovII on 03.11.2017.
 */

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Photo> mList;
    private Context context;


    public ImagesRecyclerAdapter(Context context, ArrayList<Photo> itemList) {
        this.context = context;
        this.mList = itemList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_photo, parent, false);
        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GlideApp
                .with(context)
                .load(mList.get(position).getUrls().getThumb())
                .centerCrop()
                .override(200,200)
                .placeholder(new ColorDrawable(Color.GRAY))
                .into(((ViewHolder) holder).imageView);


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
        private SquareImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
