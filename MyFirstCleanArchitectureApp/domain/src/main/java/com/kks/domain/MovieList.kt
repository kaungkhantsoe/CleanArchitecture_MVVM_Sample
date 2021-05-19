package com.kks.domain

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/

data class MovieList(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int,
)