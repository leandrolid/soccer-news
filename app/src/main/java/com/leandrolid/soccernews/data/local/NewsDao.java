package com.leandrolid.soccernews.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leandrolid.soccernews.domains.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news WHERE favorite = 1")
    LiveData<List<News>> loadAllFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(News news);
}
