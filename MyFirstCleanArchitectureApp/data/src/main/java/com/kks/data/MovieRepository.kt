package com.kks.data

import com.kks.domain.MovieRequest
import com.kks.domain.MovieListRequest

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
    fun insertMovies(list: List<MovieRequest>) = localSource.insertMovieList(list)
}

interface RemoteDataSource {
    suspend fun requestMovieListFor(page: Int): MovieListRequest
}

interface LocalDataSource {
    fun getMovieListFor(page: Int): List<MovieRequest>
    fun getMovieWith(id: Int): MovieRequest?
    fun insertMovieList(list: List<MovieRequest>)
}