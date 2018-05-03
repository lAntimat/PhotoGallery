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
import ru.lantimat.photogallery.browse.collections.CategoryListFragment;
import ru.lantimat.photogallery.browse.photos.ImagesListFragment;
import ru.lantimat.photogallery.browse.photos.ImagesRecyclerAdapter;
import ru.lantimat.photogallery.browse.photos.PhotosMVP;
import ru.lantimat.photogallery.browse.photos.Presenter;
import ru.lantimat.photogallery.photosModel.Photo;

/**
 * Created by GabdrakhmanovII on 28.07.2017.
 */

public class BrowseFragment extends Fragment {

    final static String TAG = "BrowseFragment";
    public ViewPagerAdapter adapter;
    private ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Сохраняем состояние фрагмента после пересоздания активити
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.photos_browse_fragment, null);

        viewPager = (ViewPager) v.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        addFragmentsToAdapter();
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return v;
    }

    private void addFragmentsToAdapter() {
        // Добавляем фрагменты в Adapter
        Bundle bundle;
        bundle = new Bundle();
        bundle.putString("orderBy", Presenter.SORT_LATEST);
        CategoryListFragment fragment0 = new CategoryListFragment();
        fragment0.setArguments(bundle);
        adapter.addFragment(fragment0, "Категории");


        bundle = new Bundle();
        bundle.putString(Presenter.ORDER_BY, Presenter.SORT_LATEST);
        bundle.putInt(ImagesListFragment.REQUEST_CODE, 1);
        ImagesListFragment fragment = new ImagesListFragment();
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "Последние");

        bundle = new Bundle();
        bundle.putString(Presenter.ORDER_BY, Presenter.SORT_POPULAR);
        bundle.putInt(ImagesListFragment.REQUEST_CODE, 2);
        ImagesListFragment fragment1 = new ImagesListFragment();
        fragment1.setArguments(bundle);
        adapter.addFragment(fragment1, "Популярные");

        bundle = new Bundle();
        bundle.putString(Presenter.ORDER_BY, Presenter.SORT_OLDEST);
        bundle.putInt(ImagesListFragment.REQUEST_CODE, 3);
        ImagesListFragment fragment2 = new ImagesListFragment();
        fragment2.setArguments(bundle);
        adapter.addFragment(fragment2, "Старейшие");
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
