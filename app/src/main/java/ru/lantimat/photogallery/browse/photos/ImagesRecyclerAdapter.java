package ru.lantimat.photogallery.browse.photos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.GlideApp;
import ru.lantimat.photogallery.utils.SquareImageView;

/**
 * Created by GabdrakhmanovII on 03.11.2017.
 */

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Urls> mList;
    private Context context;


    public ImagesRecyclerAdapter(Context context, ArrayList<Urls> itemList) {
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


        //SharedElement transition
        //Не получилось пока что реализовать, оставил на будущее, потому что в RecyclerView картинка квадратная, а в полноэкранном режиме
        //она показывается в исходных пропорциях и происходит не очень красивая анимация
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // the view being shared
            ViewCompat.setTransitionName(((ViewHolder)holder).imageView, "transition" + position);
        }*/



        RequestBuilder<Drawable> thumbnailRequest = GlideApp.with(context)
                .load(mList.get(position).getThumb())
                .centerCrop()
                .override(200, 200);

        GlideApp
                .with(context)
                .load(mList.get(position).getRegular())
                .thumbnail(thumbnailRequest)
                .centerCrop()
                .override(400,400)
                //.placeholder(new ColorDrawable(Color.GRAY))
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
        public SquareImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
