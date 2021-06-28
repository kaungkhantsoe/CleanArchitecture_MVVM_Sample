package com.kks.myfirstcleanarchitectureapp

import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.MovieList

const val API_KEY: String = "apikey"
const val LANGUAGE: String = "language"
const val PAGE_ONE = 1
const val PAGE_TWO = 2
const val ERROR_MSG = "error"
const val STATUS_MSG = "status message"
const val MOVIE_ID_ONE = 1
const val MOVIE_ID_MINUS = -1

fun getEmptyMovieList(): MovieList {
    return MovieList(
        PAGE_ONE,
        emptyList(),
        1,
        2
    )
}

fun sleep(second: Int = 1) {
    Thread.sleep(second * 1000L)
}

fun getMovieList(isSuccess: Boolean = true): MovieList {

    if (isSuccess) {
        return MovieList(
            PAGE_ONE,
            listOf(
                Movie(1, "title 1", "poster_path", "overview", 1),
                Movie(2, "title 2", "poster_path", "overview", 1)
            ),
            1,
            2
        )
    } else {
        return MovieList(
            null,
            null,
            null,
            null,
            0,
            "status message",
            false,
            arrayOf("error")
        )
    }
}

fun getDomainMovieList(isSuccess: Boolean = true): com.kks.domain.MovieList {
    if (isSuccess) {
        return com.kks.domain.MovieList(
            PAGE_ONE,
            listOf(
                com.kks.domain.Movie(1, "title 1", "poster_path", "overview"),
                com.kks.domain.Movie(2, "title 2", "poster_path", "overview")
            ),
            1,
            2,
        )
    } else {
        return com.kks.domain.MovieList(
            null,
            null,
            null,
            null,
            0,
            "status message",
            false,
            arrayOf("error")
        )
    }
}