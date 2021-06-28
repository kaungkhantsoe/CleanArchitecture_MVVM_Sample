package com.kks.data

import com.kks.domain.Movie
import com.kks.domain.MovieList

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localSource: LocalDataSource
) {

    suspend fun getRemoteMovieListForPage(page: Int) = remoteDataSource.requestMovieListFor(page)
    fun getLocalMovieListForPage(page: Int) = localSource.getMovieListFor(page)
    fun getMovie(id: Int) = localSource.getMovieWith(id)
    fun insertMovies(list: List<Movie>) = localSource.insertMovieList(list)
}

interface RemoteDataSource {

    suspend fun requestMovieListFor(page: Int): MovieList
}

interface LocalDataSource {
    fun getMovieListFor(page: Int): List<Movie>
    fun getMovieWith(id: Int): Movie?
    fun insertMovieList(list: List<Movie>)
}