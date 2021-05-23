package com.kks.usecases

import com.kks.data.MovieRepository
import com.kks.domain.Movie
import com.kks.domain.MovieList

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
class MovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun getMoviesFromRemote(page: Int = 1): MovieList = movieRepository.getRemoteMovieListForPage(page)
    fun getMoviesFromLocal(page: Int = 1): List<Movie> = movieRepository.getLocalMovieListForPage(page)
    fun getMovie(id: Int): Movie = movieRepository.getMovie(id)
}