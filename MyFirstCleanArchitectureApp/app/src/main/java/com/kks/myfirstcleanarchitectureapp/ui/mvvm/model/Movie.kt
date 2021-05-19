package com.kks.myfirstcleanarchitectureapp.ui.mvvm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kks.domain.Movie as DomainMovie
import com.kks.myfirstcleanarchitectureapp.common.Pageable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/

@Entity(tableName = "movie")
@Serializable
data class Movie(
    @PrimaryKey
    val id: Int,

    @SerialName("original_title")
    val originalTitle: String,

    @SerialName("poster_path")
    val posterPath: String,

    val overview: String): Pageable

fun DomainMovie.toPresentationModel(): Movie = Movie(
    id,original_title,poster_path,overview
)

fun Movie.toDomainModel(): DomainMovie = DomainMovie(
    id,originalTitle,posterPath,overview
)