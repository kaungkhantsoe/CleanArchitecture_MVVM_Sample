package com.kks.myfirstcleanarchitectureapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movie WHERE id = :id ")
    fun getMovieWith(id: Int): Movie?

    @Query("SELECT * FROM movie WHERE pageNumber = :pageNumber")
    fun getMoviesFrom(pageNumber: Int): List<Movie>

    @Query("DELETE FROM movie")
    fun deleteMovies()
}