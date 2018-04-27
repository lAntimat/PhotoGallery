package ru.lantimat.photogallery.browse.collections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import ru.lantimat.photogallery.browse.photos.ImagesListFragment;
import ru.lantimat.photogallery.collectionModel.Collection;
import ru.lantimat.photogallery.utils.ItemClickSupport;

/**
 * Created by GabdrakhmanovII on 28.07.2017.
 */

public class CategoryListFragment extends Fragment implements CollectionMVP.View {

    final static String TAG = "CollectionListFragment";

    private RecyclerView recyclerView;
    private CollectionRecyclerAdapter adapter;
    private ArrayList<Collection> ar = new ArrayList<>();
    private CollectionMVP.Presenter presenter;
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
        if(orderBy!=null) {
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

    private void initRecyclerView(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new CollectionRecyclerAdapter(getContext(), ar);
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
        paginate.setNoMoreItems(true);
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
    public void showCollections(ArrayList<ru.lantimat.photogallery.collectionModel.Collection> ar) {
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
    public void onDestroy() {
        paginate.unbind(); //Don't forget call it on onDestroy();
        super.onDestroy();
    }
}
