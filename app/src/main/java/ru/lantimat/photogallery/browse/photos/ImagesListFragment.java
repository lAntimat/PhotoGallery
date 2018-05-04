package ru.lantimat.photogallery.browse.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;
import ru.alexbykov.nopaginate.paginate.Paginate;
import ru.alexbykov.nopaginate.paginate.PaginateBuilder;
import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.Constants;
import ru.lantimat.photogallery.utils.ItemClickSupport;
import ru.lantimat.photogallery.utils.Utils;

public class ImagesListFragment extends Fragment implements PhotosMVP.View {

    final static String TAG = "ImagesListFragment";
    public static String ID = "id";

    public static String REQUEST_CODE = "requestCode";
    private int mRequestCode;

    private RecyclerView recyclerView;
    private ImagesRecyclerAdapter adapter;
    private ArrayList<Urls> ar = new ArrayList<>();
    private PhotosMVP.Presenter presenter;
    private ProgressBar progressBar;
    private Paginate paginate;
    private String orderBy;
    private String id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Сохраняем состояние фрагмента после пересоздания активити
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.images_list_fragment, null);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        initRecyclerView(v);

        mRequestCode = getArguments().getInt(REQUEST_CODE, 10);

        orderBy = getArguments().getString("orderBy", ""); //Тип сортировки. Это нужно для запросов к API
        id = getArguments().getString(ID, ""); //Для загрузки коллекции по id.

        if (savedInstanceState != null) { //Если после поворота экрана, были сохранены данные
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(Constants.PARAM_RECYCLER_STATE));
            int page = savedInstanceState.getInt(Constants.PARAM_PAGE, -1);
            orderBy = savedInstanceState.getString(Constants.PARAM_ORDER_BY);
            id = savedInstanceState.getString(Constants.PARAM_ID);


            if(!TextUtils.isEmpty(id)) presenter = new Presenter(id, ar, page, true); //Если id коллекции не пустое, передаем id, номер последней загруженной страницы и массив с картинками.
            else presenter = new Presenter(orderBy, ar, page); //иначе передаем тип сортировки, страницу и массив.
            presenter.attachView(this);
            initPaginate(true);

        } else if(!TextUtils.isEmpty(orderBy)) { //Если используем фрагмент для просмотра списка фотографий
            presenter = new Presenter(orderBy);
            presenter.attachView(this);
            presenter.getPhotos();
        } else if(!TextUtils.isEmpty(id)) { //Если используем фрагмент для просмотра списка фотографий из коллекции
            presenter = new Presenter(id, true);
            presenter.attachView(this);
            presenter.getPhotos();
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.PARAM_RECYCLER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        presenter.saveInstance(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initRecyclerView(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);


        if(Utils.isPortraitMode(getActivity())) recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        else recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));


        recyclerView.setHasFixedSize(true);
        adapter = new ImagesRecyclerAdapter(getContext(), ar);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                ImageView iv = ((ImagesRecyclerAdapter.ViewHolder)recyclerView.getChildViewHolder(v)).imageView;
                presenter.itemClick(getContext(), position, iv);

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
        if(!retainState) paginate.setNoMoreItems(true); //Костыль, чтобы анимация загрузки не показывалась, когда recyclerView пустой
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showPhotos(ArrayList<Urls> ar) {
        //stopScroll, для того, чтобы после loadMore прокрутка не продолжилась
        recyclerView.stopScroll();
        this.ar.clear();
        this.ar.addAll(ar);
        adapter.notifyDataSetChanged();
        paginate.setNoMoreItems(false);
    }

    @Override
    public void onItemClick(Intent intent, ImageView sharedImageView) {

        startActivityForResult(intent, mRequestCode);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed(Intent intent) {

    }

    @Override
    public void noMoreItems() {
        paginate.setNoMoreItems(true);
    }

    @Override
    public void onSaveInstance(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==mRequestCode) {

            ///Этот метод будет использоваться для получения загруженных данных из FullScreenImageActivity
            ArrayList<Urls> arTemp = data.getParcelableArrayListExtra(Constants.PARAM_AR);
            ar.clear();
            ar.addAll(arTemp);
            int viewPagerPosition = data.getIntExtra(Constants.PARAM_POSITION, -1);
            int page = data.getIntExtra(Constants.PARAM_PAGE, -1);
            //String orderBy = data.getStringExtra(ARG_PARAM4);

            //Создаем экземпляр Presenter и передаем ему уже ранее загруженные данные
            presenter = new Presenter(orderBy, ar, page);
            presenter.attachView(this);

            adapter.notifyDataSetChanged();

            //прокрутка списка к position, которая была у viewPager
            recyclerView.smoothScrollToPosition(viewPagerPosition);
        }
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
    public void onDestroy() {
        paginate.unbind(); //Don't forget call it on onDestroy();
        super.onDestroy();
    }
}
