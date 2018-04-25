package ru.lantimat.photogallery.browse.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ru.lantimat.photogallery.utils.ItemClickSupport;

import static ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity.ARG_PARAM1;
import static ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity.ARG_PARAM2;
import static ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity.ARG_PARAM3;
import static ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity.ARG_PARAM4;

/**
 * Created by GabdrakhmanovII on 28.07.2017.
 */

public class ImagesListFragment extends Fragment implements PhotosMVP.View {

    final static String TAG = "ImagesListFragment";
    public static String REQUEST_CODE = "requestCode";
    private int mRequestCode;

    private RecyclerView recyclerView;
    private ImagesRecyclerAdapter adapter;
    private ArrayList<Urls> ar = new ArrayList<>();
    private PhotosMVP.Presenter presenter;
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

        mRequestCode = getArguments().getInt(REQUEST_CODE, -1);

        String orderBy = getArguments().get("orderBy").toString();
        if(orderBy!=null) {
            presenter = new Presenter(orderBy);
            presenter.attachView(this);
            presenter.getPhotos();
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

        paginate = new PaginateBuilder()
                .with(recyclerView)
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        //http or db request here
                        presenter.loadMore();
                        paginate.showLoading(true);
                    }
                })
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showPhotos(ArrayList<Urls> ar) {
        this.ar.clear();
        this.ar.addAll(ar);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Intent intent, ImageView sharedImageView) {

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                sharedImageView,
                ViewCompat.getTransitionName(sharedImageView));

        startActivityForResult(intent, mRequestCode, options.toBundle());
    }

    @Override
    public void onBackPressed(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==mRequestCode) {
            ArrayList<Urls> arTemp = data.getParcelableArrayListExtra(ARG_PARAM1);
            ar.clear();
            ar.addAll(arTemp);
            int viewPagerPosition = data.getIntExtra(ARG_PARAM2, -1);
            int page = data.getIntExtra(ARG_PARAM3, -1);
            String orderBy = data.getStringExtra(ARG_PARAM4);

            presenter = new Presenter(orderBy, ar, page);
            presenter.attachView(this);

            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(viewPagerPosition);
        }
    }

    @Override
    public void showLoading() {
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        //progressBar.setVisibility(View.INVISIBLE);
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

    public class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }
}
