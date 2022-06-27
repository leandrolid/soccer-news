package com.leandrolid.soccernews.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.leandrolid.soccernews.adapter.NewsAdapter;
import com.leandrolid.soccernews.databinding.FragmentNewsBinding;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private NewsViewModel newsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        binding.rvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));

        newsListObserver();
        newsStateObserver();

        binding.srlNewsList.setOnRefreshListener(newsViewModel::getNewsFromApi);

        return binding.getRoot();
    }

    private void newsListObserver() {
        newsViewModel.getNews().observe(getViewLifecycleOwner(), news -> {
            binding.rvNewsList.setAdapter(new NewsAdapter(news, newsViewModel::saveNewsToDb));
        });
    }

    private void newsStateObserver() {
        newsViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case DOING:
                    binding.srlNewsList.setRefreshing(true);
                    break;
                case DONE:
                    binding.srlNewsList.setRefreshing(false);
                    break;
                case ERROR:
                    Snackbar.make(binding.rvNewsList, "An error occurred.", Snackbar.LENGTH_SHORT).show();
                    binding.srlNewsList.setRefreshing(false);
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