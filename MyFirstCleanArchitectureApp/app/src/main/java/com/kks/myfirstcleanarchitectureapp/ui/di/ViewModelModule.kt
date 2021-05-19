package com.kks.myfirstcleanarchitectureapp.ui.di

import com.kks.data.MovieRepository
import com.kks.myfirstcleanarchitectureapp.framework.data.Api
import com.kks.myfirstcleanarchitectureapp.framework.data.RemoteSource
import com.kks.usecases.GetMovies
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideRemoteSource(api: Api): RemoteSource {
        return RemoteSource(api)
    }

    @Provides
    fun provideMovieRepository(remoteSource: RemoteSource): MovieRepository {
        return MovieRepository(remoteSource)
    }

    @Provides
    fun provideGetMovies(movieRepository: MovieRepository): GetMovies {
        return GetMovies(movieRepository)
    }
}