package com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kks.data.MovieRepository
import com.kks.myfirstcleanarchitectureapp.MOVIE_ID_ONE
import com.kks.myfirstcleanarchitectureapp.framework.data.Api
import com.kks.myfirstcleanarchitectureapp.framework.data.RemoteSource
import com.kks.myfirstcleanarchitectureapp.framework.db.AppDb
import com.kks.myfirstcleanarchitectureapp.framework.db.LocalSource
import com.kks.myfirstcleanarchitectureapp.getMovieList
import com.kks.usecases.MovieUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class MovieRequestDetailViewModelTest {

    // region constants

    // endregion constants

    // region helper fields
    private lateinit var SUT: MovieDetailViewModel
    private val api = mock<Api>()
    private lateinit var db: AppDb
    private lateinit var remoteDataSource: RemoteSource
    private lateinit var localDataSource: LocalSource
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieUseCase: MovieUseCase
    // endregion helper fields

    @Before
    fun setup() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDb::class.java)
            .allowMainThreadQueries().build()
        remoteDataSource = RemoteSource(api)
        localDataSource = LocalSource(db)
        movieRepository = MovieRepository(remoteDataSource, localDataSource)
        movieUseCase = MovieUseCase(movieRepository)
        SUT = MovieDetailViewModel(movieUseCase)

    }

    @Test
    fun getDetail_Success_ReturnMovie() {
        // Given
        db.MovieDao().insertMovies(getMovieList(true).results!!)

        // When
        val actual = SUT.getDetail(MOVIE_ID_ONE)

        // Then
        val expected = getMovieList(true).results!!.first { it.id == MOVIE_ID_ONE }
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getDetail_Failure_ReturnNull() {
        // Given

        // When
        val actual = SUT.getDetail(MOVIE_ID_ONE)

        // Then
        assertThat(actual).isNull()
    }

    @After
    fun tearDown() {
        db.close()
    }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}