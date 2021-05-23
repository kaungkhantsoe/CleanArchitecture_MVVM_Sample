package com.kks.myfirstcleanarchitectureapp.framework.db

import com.kks.data.LocalDataSource
import com.kks.domain.Movie as DomainMovie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toDomainModel
import javax.inject.Inject

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
class LocalSource
@Inject constructor(
    private val db: AppDb
): LocalDataSource
{
    override fun getMovieListFor(page: Int): List<DomainMovie> {
        return db.MovieDao().getMoviesFrom(page).map(Movie::toDomainModel)
    }

    override fun getMovieWith(id: Int): com.kks.domain.Movie {
        return db.MovieDao().getMovieWith(id).toDomainModel()
    }
}