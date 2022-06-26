package com.leandrolid.soccernews.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.leandrolid.soccernews.adapter.NewsAdapter;
import com.leandrolid.soccernews.data.local.SoccerNewsDb;
import com.leandrolid.soccernews.databinding.FragmentNewsBinding;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private SoccerNewsDb db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // TODO Improve db implementation to avoid "allowMainThreadQueries"
        db = Room
                .databaseBuilder(requireContext(), SoccerNewsDb.class, "soccer-news")
                .allowMainThreadQueries()
                .build();

        setupRecyclerView(newsViewModel);
        return root;
    }

    private void setupRecyclerView(NewsViewModel newsViewModel) {
        binding.rvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        LifecycleOwner recyclerViewOwner = getViewLifecycleOwner();

        newsViewModel.getNews().observe(
                recyclerViewOwner,
                news -> binding.rvNewsList.setAdapter(new NewsAdapter(news, (updatedNews) -> {
                    Log.i("favorited", updatedNews.toString());
                    db.newsDao().save(updatedNews);
                })));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}