package ru.lantimat.photogallery.ui.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;
import ru.alexbykov.nopaginate.paginate.Paginate;
import ru.alexbykov.nopaginate.paginate.PaginateBuilder;
import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.models.Collection;
import ru.lantimat.photogallery.utils.ArraySaveHelper;
import ru.lantimat.photogallery.utils.Constants;
import ru.lantimat.photogallery.utils.ItemClickSupport;
import ru.lantimat.photogallery.utils.Utils;

/**
 * Created by GabdrakhmanovII on 28.07.2017.
 */

public class CategoryListFragment extends Fragment implements CategoryMVP.View {

    final static String TAG = "CollectionListFragment";

    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter adapter;
    private ArrayList<Collection> ar = new ArrayList<>();
    private CategoryMVP.Presenter presenter;
    private ProgressBar progressBar;
    private Paginate paginate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.images_list_fragment, null);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        initRecyclerView(v);

        String orderBy = getArguments().get("orderBy").toString();

        if (savedInstanceState != null) { //Если фрагмент
            //ar.addAll(savedInstanceState.getParcelableArrayList(Constants.PARAM_AR));

            ArraySaveHelper saveHelper = new ArraySaveHelper();
            ar.addAll(saveHelper.getArrayList(getContext(), Constants.PARAM_AR));

            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(Constants.PARAM_RECYCLER_STATE));
            int page = savedInstanceState.getInt(Constants.PARAM_PAGE, -1);
            orderBy = savedInstanceState.getString(Constants.PARAM_ORDER_BY);

            presenter = new Presenter(orderBy, ar, page);
            presenter.attachView(this);
            initPaginate(true);
        } else if(orderBy!=null) {
            presenter = new Presenter(orderBy);
            presenter.attachView(this);
            presenter.getCollections();
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.PARAM_RECYCLER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        presenter.saveInstance(getContext(), outState);
    }

    private void initRecyclerView(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);
        if(Utils.isPortraitMode(getActivity())) recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        else recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new CategoryRecyclerAdapter(getContext(), ar);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                presenter.itemClick(getContext(), position);
            }
        });

        //На всякий случай, пока что перешел на библиотеку
        //Загрузка следующей страницы
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int firstVisible = layoutManager.findFirstVisibleItemPosition();
                int visibleCount = Math.abs(firstVisible - layoutManager.findLastVisibleItemPosition());
                int itemCount = recyclerView.getAdapter().getItemCount();

                //Если position первого видимого item и количества видимых элементов при сложении дают число больше,
                // чем общее количество item, то значит мы в конце списка. Поэтому грузим следующую страницу
                if ((firstVisible + visibleCount + 1) >= itemCount) {
                }
            }
        });*/

        initPaginate(false);
    }

    private void initPaginate(boolean retainState) {
        paginate = new PaginateBuilder()
                .with(recyclerView)
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        //http or db request here
                        presenter.loadMore();
                    }
                })
                .build();
        if(!retainState) paginate.setNoMoreItems(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading(boolean isLoadMore) {
        if(isLoadMore) paginate.showLoading(true);
        else progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        paginate.showLoading(false);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        paginate.showError(true);
    }

    @Override
    public void showCollections(ArrayList<Collection> ar) {
        //stopScroll, для того, чтобы после loadMore прокрутка не продолжилась
        recyclerView.stopScroll();
        this.ar.clear();
        this.ar.addAll(ar);
        adapter.notifyDataSetChanged();
        paginate.setNoMoreItems(false);
    }

    @Override
    public void showCategoryImagesList(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onSaveInstance(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onDestroy() {
        paginate.unbind(); //Don't forget call it on onDestroy();
        super.onDestroy();
    }
}
