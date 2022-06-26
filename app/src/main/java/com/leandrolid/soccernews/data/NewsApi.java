package com.leandrolid.soccernews.data;

import com.leandrolid.soccernews.domains.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {

    @GET("news.json")
    Call<List<News>> getMatches();
}
