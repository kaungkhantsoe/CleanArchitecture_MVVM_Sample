package com.kks.myfirstcleanarchitectureapp.ui.mvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kks.domain.MovieRequest as DomainMovie
import com.kks.myfirstcleanarchitectureapp.common.Pageable

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val id: Int,

    val originalTitle: String,

    val posterPath: String,

    val overview: String,

    var pageNumber: Int? = 1
    ): Pageable

fun DomainMovie.toPresentationModel(): Movie = Movie(
    id,original_title ?: "",poster_path ?: "",overview ?: ""
)

fun Movie.toDomainModel(): DomainMovie = DomainMovie(
    id,originalTitle,posterPath,overview
)

