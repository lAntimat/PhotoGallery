package ru.lantimat.photogallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import ru.lantimat.photogallery.browse.CollectionFragment;
import ru.lantimat.photogallery.browse.collections.CategoryListFragment;
import ru.lantimat.photogallery.browse.PhotosFragment;
import ru.lantimat.photogallery.utils.NonSwipeableViewPager;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";
    static final int PAGE_COUNT = 1;

    private NonSwipeableViewPager pager;
    private PagerAdapter pagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Обзор");
        //getSupportActionBar().setSubtitle("MainActivity");
        //setupBottomBar();
        initViewPager();
    }



    private void setupBottomBar() { //Инициализация нижнего бара
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);


        //Создаем айтемы
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottom_bar_collection, R.drawable.bg_personal, R.color.colorBottomNavigationPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_bar_photo, R.drawable.bg_personal, R.color.colorBottomNavigationPrimary);
        //AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_instagram_white_24dp, R.color.colorBottomNavigationPrimary);
        //AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_dots_horizontal_white_24dp, R.color.colorBottomNavigationPrimary);

        //Добавляем в бар
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        //bottomNavigation.addItem(item3);
        //bottomNavigation.addItem(item4);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setBehaviorTranslationEnabled(false);

        //bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.md_blue_900));

        // Set background color
        //bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Add or remove notification for each item
        //bottomNavigation.setNotification("1", 2);



        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                pager.setCurrentItem(position);
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        //Устанавливаем текущий элемент
        bottomNavigation.setCurrentItem(0,false);

    }

    private void initViewPager() {

        //Инициализация ViewPager
        pager = findViewById(R.id.viewPager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PhotosFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
}
