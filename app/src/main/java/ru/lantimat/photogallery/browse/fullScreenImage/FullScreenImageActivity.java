package ru.lantimat.photogallery.browse.fullScreenImage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.ArrayList;

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.browse.photos.PhotosMVP;
import ru.lantimat.photogallery.browse.photos.Presenter;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.Constants;

public class FullScreenImageActivity extends AppCompatActivity implements PhotosMVP.View {

    private ArrayList<Urls> ar;
    private int viewPagerPosition = 0;
    private int page = -1;
    private String orderBy;

    private GalleryPagerAdapter adapter;
    private Presenter presenter;
    private ViewPager viewPager;
    private boolean isNoMoreItems = false;

    @Override
    public boolean onSupportNavigateUp() {
        presenter.backPressed(this, viewPager.getCurrentItem());
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_photo_full_size);

        ar = new ArrayList<>();

        if (getIntent().getExtras() != null) {
            ar = getIntent().getParcelableArrayListExtra(Constants.PARAM_AR);
            viewPagerPosition = getIntent().getIntExtra(Constants.PARAM_POSITION, -1);
            page = getIntent().getIntExtra(Constants.PARAM_PAGE, -1);
            orderBy = getIntent().getStringExtra(Constants.PARAM_ORDER_BY);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new GalleryPagerAdapter(getSupportFragmentManager(), ar);

        initViewPager();

        presenter = new Presenter(orderBy, ar, page);
        presenter.attachView(this);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewPagerPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getSupportActionBar().setTitle(position +1 + "/" + ar.size());

            }

            @Override
            public void onPageSelected(int position) {
                if(position + 3 >= ar.size() & !isNoMoreItems) presenter.loadMore();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showPhotos(ArrayList<Urls> ar) {
        this.ar.clear();
        this.ar.addAll(ar);
        adapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(viewPager.getCurrentItem() + 1 + "/" + ar.size());
    }

    @Override
    public void onItemClick(Intent intent, ImageView imageView) {

    }

    @Override
    public void onBackPressed(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);

    }

    @Override
    public void noMoreItems() {
        isNoMoreItems = true;
    }

    @Override
    public void onSaveInstance(Bundle bundle) {

    }

    @Override
    public void showLoading(boolean isLoadMore) {

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
        if(presenter!=null) presenter.backPressed(this, viewPager.getCurrentItem());
    }
}
