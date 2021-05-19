package com.kks.myfirstcleanarchitectureapp.ui.mvvm.model

import kotlinx.serialization.Serializable
import com.kks.domain.MovieList as DomainMovieList
import com.kks.domain.Movie as DomainMovie

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
@Serializable
data class MovieList(val page: Int, val results: List<Movie>, val total_pages: Int, val total_results: Int)

fun DomainMovieList.toPresentationModel(): MovieList = MovieList (
    page,
    results.map(DomainMovie::toPresentationModel),
    total_pages,
    total_results
)

fun MovieList.toDomainModel(): DomainMovieList = DomainMovieList (
    page,
    results.map(Movie::toDomainModel),
    total_pages,
    total_results,

)