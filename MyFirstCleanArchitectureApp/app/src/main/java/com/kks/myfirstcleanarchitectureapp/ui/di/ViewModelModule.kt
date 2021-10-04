package com.kks.myfirstcleanarchitectureapp.ui.di

import com.kks.data.MovieRepository
import com.kks.myfirstcleanarchitectureapp.framework.data.Api
import com.kks.myfirstcleanarchitectureapp.framework.data.RemoteSource
import com.kks.myfirstcleanarchitectureapp.framework.db.AppDb
import com.kks.myfirstcleanarchitectureapp.framework.db.LocalSource
import com.kks.myfirstcleanarchitectureapp.ui.BaseApplication
import com.kks.myfirstcleanarchitectureapp.ui.util.NetworkListener
import com.kks.myfirstcleanarchitectureapp.ui.util.NetworkUtil
import com.kks.usecases.MovieUseCase
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
    fun provideNetworkUtil(context: BaseApplication): NetworkListener {
        return NetworkUtil(context)
    }

    @Provides
    fun provideAppDb(context: BaseApplication): AppDb {
        return AppDb.getDatabase(context)
    }

    @Provides
    fun provideRemoteSource(api: Api): RemoteSource {
        return RemoteSource(api)
    }

    @Provides
    fun provideLocalSource(appDb: AppDb): LocalSource {
        return LocalSource(appDb)
    }

    @Provides
    fun provideMovieRepository(remoteSource: RemoteSource, localSource: LocalSource): MovieRepository {
        return MovieRepository(remoteSource, localSource)
    }

    @Provides
    fun provideMovieUseCase(movieRepository: MovieRepository): MovieUseCase {
        return MovieUseCase(movieRepository)
    }
}