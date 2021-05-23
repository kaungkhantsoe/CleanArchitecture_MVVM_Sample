package com.kks.myfirstcleanarchitectureapp.ui.mvvm.model

import kotlinx.serialization.Serializable
import com.kks.domain.MovieList as DomainMovieList

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
@Serializable
data class MovieList(val page: Int, val results: List<Movie>, val total_pages: Int, val total_results: Int)

fun DomainMovieList.toPresentationModel(): MovieList = MovieList (
    page,
    results.map {
        return@map Movie(it.id,it.original_title,it.poster_path,it.overview,page)
    },
    total_pages,
    total_results
)

fun MovieList.toDomainModel(): DomainMovieList = DomainMovieList (
    page,
    results.map(Movie::toDomainModel),
    total_pages,
    total_results,

)