package ru.lantimat.photogallery.browse.fullScreenImage;

/**
 * Created by GabdrakhmanovII on 06.12.2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chuross.flinglayout.FlingLayout;


import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;
import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.GlideApp;


public class FullscreenImageAdapter extends PagerAdapter {

    final String TAG = "FullScreenImageAdapter";

    public static int fullScreenImagePosition;
    private Context context;
    private ArrayList<Urls> ar;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private TextView textView;
    private Context ctx;
    private int transitionName;
    private FlingLayout flingLayout;
    private PhotoView imgDisplay;
    private RequestListener requestListener;
    private SimpleTarget simpleTarget;

    public FullscreenImageAdapter(Context context, ArrayList<Urls> ar) {
        this.context = context;
        this.ar = ar;
    }

    public FullscreenImageAdapter(Context context, ArrayList<Urls> ar, int transitionName) {
        this.context = context;
        this.ar = ar;
        this.transitionName = transitionName;
    }

    @Override
    public int getCount() {
        return this.ar.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        fullScreenImagePosition = position;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.viewpager_item_image, container,
                false);

        textView = v.findViewById(R.id.textView);
        progressBar = v.findViewById(R.id.progressBar);

        initFlingLayout(v);
        initImageView(v);


        //parse uri
        Uri thumbUri = Uri.parse(ar.get(position).getThumb());
        Uri fullUri = Uri.parse(ar.get(position).getRegular());

        // setup Glide request without the into() method
        RequestBuilder<Drawable> thumbnailRequest = GlideApp
                .with(context)
                .load(thumbUri);


        //progressBar.setVisibility(View.VISIBLE);
        GlideApp
                .with(context)
                .load(fullUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //.thumbnail(thumbnailRequest)
                //.listener(requestListener)
                .thumbnail(0.3f)
                .override(1024, 1024)
                .into(imgDisplay);


        ((ViewPager) container).addView(v);

        return v;
    }

    private void initFlingLayout(View v) {
        //FlingLayout для поддержки закрытия изображения свайпом вверх/вниз

        flingLayout = v.findViewById(R.id.fling_layout);
        flingLayout.setBackgroundColor(Color.argb(Math.round(230), 0, 0, 0));

        flingLayout.setDismissListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                //Toast.makeText(context, "dismiss!!", Toast.LENGTH_LONG).show();
                ((FullScreenImageActivity) context).onBackPressed();
                return Unit.INSTANCE;
            }
        });

        flingLayout.setPositionChangeListener(new Function3<Integer, Integer, Float, Unit>() {
            @Override
            public Unit invoke(Integer top, Integer left, Float dragRangeRate) {
                flingLayout.setBackgroundColor(Color.argb(Math.round(230 * (1.0F - dragRangeRate)), 0, 0, 0));
                return Unit.INSTANCE;
            }
        });
    }

    private void initImageView(View v) {
        //Кастомный ImageView с поддержкой зума
        imgDisplay = v.findViewById(R.id.photo_view);
        imgDisplay.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                flingLayout.setDragEnabled(scaleFactor <= 1F);
            }
        });
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
