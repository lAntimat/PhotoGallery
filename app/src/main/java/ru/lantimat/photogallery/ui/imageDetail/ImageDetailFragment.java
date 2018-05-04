package ru.lantimat.photogallery.ui.imageDetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;

import ru.lantimat.photogallery.R;
import ru.lantimat.photogallery.models.Urls;
import ru.lantimat.photogallery.utils.GlideApp;

public class ImageDetailFragment extends Fragment {

    private static final String EXTRA_IMAGE = "urls";
    private PhotoView imgDisplay;

    public ImageDetailFragment() {
        // Required empty public constructor
    }

    public static ImageDetailFragment newInstance(Urls image) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.viewpager_item_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Urls urls = getArguments().getParcelable(EXTRA_IMAGE);

        final PhotoView imageView = view.findViewById(R.id.photo_view);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        initImageView(view);

        //Запрос для загрузки thumbnail
        RequestBuilder<Drawable> thumbnailRequest = GlideApp.with(getActivity()).load(urls.getThumb());

        //Загрузка основной картинки
        GlideApp.with(getActivity())
                .load(urls.getRegular())
                .thumbnail(thumbnailRequest)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        startPostponedEnterTransition();
                        imageView.setImageDrawable(resource);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void initImageView(View v) {
        //Кастомный ImageView с поддержкой зума
        imgDisplay = v.findViewById(R.id.photo_view);
    }
}
