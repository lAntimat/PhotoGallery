package ru.lantimat.photogallery.browse.photos;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.lantimat.photogallery.R;

public class CategoryImagesListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoty_images_list);
        showFragment();
    }

    private void showFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ImagesListFragment.ID, getIntent().getStringExtra(ImagesListFragment.ID));

        ImagesListFragment fragment = new ImagesListFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }
}
