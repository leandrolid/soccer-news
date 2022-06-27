package com.leandrolid.soccernews.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leandrolid.soccernews.adapter.NewsAdapter;
import com.leandrolid.soccernews.databinding.FragmentFavoritesBinding;
import com.leandrolid.soccernews.domains.News;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel favoritesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loadFavoriteNews();
        return root;
    }

    private void loadFavoriteNews() {
        binding.rvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        LiveData<List<News>> favoriteLiveData = favoritesViewModel.getFavoritesFromDb();

        favoriteLiveData.observe(getViewLifecycleOwner(), favoriteNews -> {
            binding.rvNewsList.setAdapter(new NewsAdapter(favoriteNews, (updatedNews) -> {
                favoritesViewModel.saveNewsToDb(updatedNews);
                loadFavoriteNews();
            }));
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}