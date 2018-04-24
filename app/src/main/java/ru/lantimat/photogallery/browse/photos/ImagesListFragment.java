package ru.lantimat.photogallery.browse.photos;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.Fade;
import android.support.transition.TransitionSet;
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

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.browse.fullScreenImage.FragmentFullscreenImage;
import ru.lantimat.photogallery.browse.fullScreenImage.FullSizeImageListener;
import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.utils.ItemClickSupport;

/**
 * Created by GabdrakhmanovII on 28.07.2017.
 */

public class ImagesListFragment extends Fragment implements PhotosMVP.View {

    final static String TAG = "ImagesListFragment";

    private RecyclerView recyclerView;
    private ImagesRecyclerAdapter adapter;
    private ArrayList<Photo> ar = new ArrayList<>();
    private PhotosMVP.Presenter presenter;
    private ProgressBar progressBar;
    private FullSizeImageListener fullSizeImageListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.images_list_fragment, null);
        progressBar = v.findViewById(R.id.progressBar);
        initRecyclerView(v);

        String orderBy = getArguments().get("orderBy").toString();
        if(orderBy!=null) {
            presenter = new Presenter(orderBy);
            presenter.attachView(this);
            presenter.getPhotos();
        }

        return v;
    }

    public void registerFullSizeImageListener(FullSizeImageListener listener) {
        fullSizeImageListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initRecyclerView(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new ImagesRecyclerAdapter(getContext(), ar);
        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });

        //Загрузка следующей страницы
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    presenter.loadMore();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showPhotos(ArrayList<Photo> ar) {
        if(fullSizeImageListener!=null) fullSizeImageListener.onAdd(ar); //обновляем массив в Фрагменте
        this.ar.clear();
        this.ar.addAll(ar);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, ArrayList<Photo> ar) {

        FragmentFullscreenImage fragment = FragmentFullscreenImage.newInstance(position, ar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
        }

        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(((ImagesRecyclerAdapter.ViewHolder) holder).imageView, "testTransition")
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
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
