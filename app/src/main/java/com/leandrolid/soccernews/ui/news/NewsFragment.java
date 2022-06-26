package com.leandrolid.soccernews.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.leandrolid.soccernews.adapter.NewsAdapter;
import com.leandrolid.soccernews.databinding.FragmentNewsBinding;
import com.leandrolid.soccernews.ui.MainActivity;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupSwipeRefreshLayout();
        setupRecyclerView();
        return root;
    }

    private void setupSwipeRefreshLayout() {
        binding.srlNewsList.setOnRefreshListener(this::setupRecyclerView);
    }

    private void setupRecyclerView() {
        NewsViewModel newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding.rvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        LifecycleOwner recyclerViewOwner = getViewLifecycleOwner();

        setupNewsList(newsViewModel, recyclerViewOwner);
        onNewsStateChange(newsViewModel, recyclerViewOwner);
    }

    private void setupNewsList(NewsViewModel newsViewModel, LifecycleOwner recyclerViewOwner) {
        newsViewModel.getNews().observe(
                recyclerViewOwner,
                news -> binding.rvNewsList.setAdapter(new NewsAdapter(news, (updatedNews) -> {
//                    Log.i("favorite", updatedNews.toString());
                    MainActivity activity = (MainActivity) getActivity();

                    assert activity != null;
                    activity.getDb().newsDao().save(updatedNews);
                })));
    }

    private void onNewsStateChange(NewsViewModel newsViewModel, LifecycleOwner recyclerViewOwner) {
        newsViewModel.getState().observe(
                recyclerViewOwner,
                state -> {
                    switch (state) {
                        case DONE:
                            binding.srlNewsList.setRefreshing(false);
                            break;
                        case DOING:
                            binding.srlNewsList.setRefreshing(true);
                            break;
                        case ERROR:
                            binding.srlNewsList.setRefreshing(false);
                            Snackbar.make(binding.rvNewsList, "An error occurred.", Snackbar.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}