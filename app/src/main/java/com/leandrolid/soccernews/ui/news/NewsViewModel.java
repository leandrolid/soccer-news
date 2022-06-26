package com.leandrolid.soccernews.ui.news;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.leandrolid.soccernews.data.NewsApi;
import com.leandrolid.soccernews.domains.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsViewModel extends ViewModel {

    private final MutableLiveData<List<News>> news = new MutableLiveData<>();
    private final NewsApi soccerNewsApi;

    public NewsViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://digitalinnovationone.github.io/soccer-news-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        soccerNewsApi = retrofit.create(NewsApi.class);
        getNewsFromApi();
    }

    private void getNewsFromApi() {
        soccerNewsApi.getMatches().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(@NonNull Call<List<News>> call, @NonNull Response<List<News>> response) {
                if (response.isSuccessful()) {
                    news.setValue(response.body());
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<News>> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }

    private void showErrorMessage() {
//      Snackbar.make(binding.fabSimulate, message, Snackbar.LENGTH_SHORT).show()
        Log.e("getApi", "An error occurred.");
    }

    public MutableLiveData<List<News>> getNews() {
        return this.news;
    }
}