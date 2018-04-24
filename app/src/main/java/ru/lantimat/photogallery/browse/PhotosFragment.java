package ru.lantimat.photogallery.browse;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ru.lantimat.photogallery.MainActivity;
import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.browse.photos.ImagesListFragment;
import ru.lantimat.photogallery.browse.photos.ImagesRecyclerAdapter;
import ru.lantimat.photogallery.browse.photos.PhotosMVP;
import ru.lantimat.photogallery.browse.photos.Presenter;
import ru.lantimat.photogallery.photosModel.Photo;

/**
 * Created by GabdrakhmanovII on 28.07.2017.
 */

public class PhotosFragment extends Fragment {

    final static String TAG = "PhotosFragment";
    public ViewPagerAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.photos_browse_fragment, null);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        // Add Fragments to adapter one by one
        Bundle bundle;
        bundle = new Bundle();
        bundle.putString("orderBy", Presenter.SORT_LATEST);
        ImagesListFragment fragment = new ImagesListFragment();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "Последние");

        bundle = new Bundle();
        bundle.putString("orderBy", Presenter.SORT_POPULAR);
        ImagesListFragment fragment1 = new ImagesListFragment();
        fragment1.setArguments(bundle);
        adapter.addFragment(fragment1, "Популярные");

        bundle = new Bundle();
        bundle.putString("orderBy", Presenter.SORT_OLDEST);
        ImagesListFragment fragment2 = new ImagesListFragment();
        fragment2.setArguments(bundle);
        adapter.addFragment(fragment2, "Старейшие");

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void initToolbar() {
        if(getActivity()!=null) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.bottom_bar_photo);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) initToolbar();
    }

    // Adapter for the viewpager using FragmentPagerAdapter
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public Fragment getFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
