package com.kks.myfirstcleanarchitectureapp.framework.db

import com.kks.data.LocalDataSource
import com.kks.domain.MovieRequest as DomainMovie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toDomainModel
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toPresentationModel
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

    override fun getMovieWith(id: Int): DomainMovie? {
        return db.MovieDao().getMovieWith(id)?.toDomainModel()
    }

    override fun insertMovieList(list: List<DomainMovie>) {
        db.MovieDao().insertMovies(list.map(DomainMovie::toPresentationModel))
    }
}