package ru.lantimat.photogallery.browse.fullScreenImage;

/**
 * Created by GabdrakhmanovII on 06.12.2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
        PhotoView imgDisplay;
        Button btnClose;

        fullScreenImagePosition = position;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.viewpager_item_image, container,
                false);
        final FlingLayout flingLayout = viewLayout.findViewById(R.id.fling_layout);
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

        imgDisplay = viewLayout.findViewById(R.id.photo_view);

        imgDisplay.setOnScaleChangeListener(new OnScaleChangedListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                flingLayout.setDragEnabled(scaleFactor <= 1F);
            }
        });

        textView = viewLayout.findViewById(R.id.textView);
        progressBar = viewLayout.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        //textView.setText(position + "/" + ar.size());

        Uri uri = Uri.parse(ar.get(position).getThumb());
        /*GlideApp.with(context)
                .load(uri)
                .into(imgDisplay);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgDisplay.setTransitionName("transition" + position);
        }

        GlideApp
                .with(context)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //if(position==transitionName)
                            //((FullScreenImageActivity) context).startPostponedEnterTransition();
                        }
                        return false;
                    }
                })
                .into(imgDisplay);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
