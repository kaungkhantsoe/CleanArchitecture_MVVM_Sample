package com.kks.myfirstcleanarchitectureapp.framework.data

import com.kks.data.RemoteDataSource
import com.kks.domain.MovieList
import com.kks.myfirstcleanarchitectureapp.ui.common.Constants.API_KEY
import com.kks.myfirstcleanarchitectureapp.ui.common.Constants.language
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toDomainModel
import javax.inject.Inject

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
open class RemoteSource
@Inject
constructor(
    private val api: Api
) : RemoteDataSource {

    override suspend fun requestMovieListFor(page: Int): MovieList {
        return api.getMovies(API_KEY, language, page).toDomainModel()
    }
}
