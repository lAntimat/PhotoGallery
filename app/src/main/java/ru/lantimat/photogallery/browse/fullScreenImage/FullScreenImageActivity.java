package ru.lantimat.photogallery.browse.fullScreenImage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.browse.photos.PhotosMVP;
import ru.lantimat.photogallery.browse.photos.Presenter;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.GlideApp;
import ru.lantimat.photogallery.utils.WrapContentViewPager;

public class FullScreenImageActivity extends AppCompatActivity implements PhotosMVP.View {

    public static final String ARG_PARAM1 = "ar";
    public static final String ARG_PARAM2 = "position";
    public static final String ARG_PARAM3 = "page";
    public static final String ARG_PARAM4 = "orderBy";
    public static final String EXTRA_ANIMAL_ITEM = "extraItem";
    public static final String EXTRA_ANIMAL_IMAGE_TRANSITION_NAME = "transitionName";

    private WrapContentViewPager viewPager;
    private FullscreenImageAdapter adapter;
    private ArrayList<Urls> ar;
    private TextView textView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private int viewPagerPosition = 0;
    private int page = -1;
    private String orderBy;
    private PhotosMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_full_size);

        ar = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            ar = getIntent().getParcelableArrayListExtra(ARG_PARAM1);
            viewPagerPosition = getIntent().getIntExtra(ARG_PARAM2, -1);
            page = getIntent().getIntExtra(ARG_PARAM3, -1);
            orderBy = getIntent().getStringExtra(ARG_PARAM4);
        }

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getStringExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
            //imageView.setTransitionName(imageTransitionName);
            adapter = new FullscreenImageAdapter(this, ar, viewPagerPosition);
        } else {
            adapter = new FullscreenImageAdapter(this, ar);
        }

        initViewPager();

        if (orderBy != null) {
            presenter = new Presenter(orderBy, ar, page);
            presenter.attachView(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getStringExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
            imageView.setTransitionName(imageTransitionName);

            GlideApp
                    .with(this)
                    .load(ar.get(viewPagerPosition).getThumb())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(imageView);

            Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
            sharedElementEnterTransition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    imageView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();


    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setVisibility(View.INVISIBLE);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewPagerPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPosition = position;
                textView.setText(position + 1 + "/" + viewPager.getAdapter().getCount());
                if(position + 3 == viewPager.getAdapter().getCount())
                    presenter.loadMore();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        textView.setText(viewPagerPosition + 1 + "/" + viewPager.getAdapter().getCount());
    }


    @Override
    public void showPhotos(ArrayList<Urls> ar) {
        this.ar.clear();
        this.ar.addAll(ar);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Intent intent, ImageView imageView) {

    }

    @Override
    public void onBackPressed(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        presenter.backPressed(this, viewPager.getCurrentItem());
    }
}
