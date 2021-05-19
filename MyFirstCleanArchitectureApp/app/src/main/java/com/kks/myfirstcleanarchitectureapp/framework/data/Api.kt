package com.kks.myfirstcleanarchitectureapp.framework.data

import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.MovieList
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
interface Api {

    @GET("movie/now_playing")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MovieList
}