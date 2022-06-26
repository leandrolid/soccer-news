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

import com.leandrolid.soccernews.MainActivity;
import com.leandrolid.soccernews.adapter.NewsAdapter;
import com.leandrolid.soccernews.databinding.FragmentNewsBinding;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        return root;
    }

    private void setupRecyclerView() {
        NewsViewModel newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding.rvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        LifecycleOwner recyclerViewOwner = getViewLifecycleOwner();

        newsViewModel.getNews().observe(
                recyclerViewOwner,
                news -> binding.rvNewsList.setAdapter(new NewsAdapter(news, (updatedNews) -> {
//                    Log.i("favorite", updatedNews.toString());
                    MainActivity activity = (MainActivity) getActivity();

                    assert activity != null;
                    activity.getDb().newsDao().save(updatedNews);
                })));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}