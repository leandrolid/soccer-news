package com.leandrolid.soccernews.ui.news;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leandrolid.soccernews.data.SoccerNewsRepository;
import com.leandrolid.soccernews.domains.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsViewModel extends ViewModel {

    private final MutableLiveData<State> state = new MutableLiveData<>();
    private final MutableLiveData<List<News>> news = new MutableLiveData<>();

    public NewsViewModel() {
        getNewsFromApi();
    }

    public void getNewsFromApi() {
        state.setValue(State.DOING);
        SoccerNewsRepository.getInstance().getRemoteApi().getMatches().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                if (response.isSuccessful()) {
                    news.setValue(response.body());
                    state.setValue(State.DONE);
                } else {
                    state.setValue(State.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                state.setValue(State.ERROR);
            }
        });
    }

    public void saveNewsToDb(News news) {
        AsyncTask.execute(() -> SoccerNewsRepository.getInstance().getLocalDb().newsDao().save(news));
    }

    public MutableLiveData<List<News>> getNews() {
        return this.news;
    }

    public MutableLiveData<State> getState() {
        return this.state;
    }

    public enum State {
        DOING, DONE, ERROR
    }
}