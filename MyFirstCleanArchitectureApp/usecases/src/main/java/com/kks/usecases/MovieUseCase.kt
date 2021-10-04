package com.kks.usecases

import com.kks.data.MovieRepository
import com.kks.domain.MovieRequest
import com.kks.domain.MovieListRequest

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
class MovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun getMoviesFromRemote(page: Int = 1): MovieListRequest = movieRepository.getRemoteMovieListForPage(page)
    fun getMoviesFromLocal(page: Int = 1): List<MovieRequest> = movieRepository.getLocalMovieListForPage(page)
    fun getMovie(id: Int): MovieRequest? = movieRepository.getMovie(id)
    fun insertMovies(list: List<MovieRequest>) = movieRepository.insertMovies(list)
}