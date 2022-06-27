package com.leandrolid.soccernews.ui.favorites;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.leandrolid.soccernews.data.SoccerNewsRepository;
import com.leandrolid.soccernews.domains.News;

import java.util.List;

public class FavoritesViewModel extends ViewModel {

    public FavoritesViewModel() {
    }

    public void saveNewsToDb(News news) {
        AsyncTask.execute(() -> SoccerNewsRepository.getInstance().getLocalDb().newsDao().save(news));
    }

    public LiveData<List<News>> getFavoritesFromDb() {
        return SoccerNewsRepository.getInstance().getLocalDb().newsDao().loadAllFavorites();
    }
}