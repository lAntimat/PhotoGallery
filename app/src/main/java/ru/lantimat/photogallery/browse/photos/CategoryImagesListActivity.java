package ru.lantimat.photogallery.browse.photos;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ru.lantimat.photogallery.R;

public class CategoryImagesListActivity extends AppCompatActivity {

    public static String TITLE = "title";
    private final String IMAGE_LIST_FRAGMENT_TAG = "imageListTag";

    private ImagesListFragment fragment;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoty_images_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String title = getIntent().getStringExtra(TITLE);
        getSupportActionBar().setTitle(title);

        showFragment();
    }

    private void showFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ImagesListFragment.ID, getIntent().getStringExtra(ImagesListFragment.ID));
        bundle.putInt(ImagesListFragment.REQUEST_CODE, 5);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (ImagesListFragment) fm.findFragmentByTag(IMAGE_LIST_FRAGMENT_TAG);

        // create the fragment and data the first time
        if (fragment == null) {
            // add the fragment
            fragment = new ImagesListFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(fragment, IMAGE_LIST_FRAGMENT_TAG);
            ft.replace(R.id.container, fragment, IMAGE_LIST_FRAGMENT_TAG).commit();
        }
    }
}
