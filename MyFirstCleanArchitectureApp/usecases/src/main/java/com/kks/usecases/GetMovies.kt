package com.kks.usecases

import com.kks.data.MovieRepository
import com.kks.domain.MovieList

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
class GetMovies(private val movieRepository: MovieRepository) {
    suspend fun run(page: Int = 1): MovieList = movieRepository.getMovieListForPage(page)
}