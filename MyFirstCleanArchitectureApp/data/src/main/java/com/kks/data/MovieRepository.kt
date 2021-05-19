package com.kks.data

import com.kks.domain.MovieList

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
class MovieRepository(
    private val movieDataSource: MovieDataSource
) {

    suspend fun getMovieListForPage(page: Int) = movieDataSource.requestMovieListFor(page)
}

interface MovieDataSource {

    suspend fun requestMovieListFor(page: Int): MovieList
}