package com.kks.myfirstcleanarchitectureapp.ui.mvvm.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kks.data.MovieRepository
import com.kks.myfirstcleanarchitectureapp.framework.data.Api
import com.kks.myfirstcleanarchitectureapp.framework.data.RemoteSource
import com.kks.myfirstcleanarchitectureapp.framework.db.AppDb
import com.kks.myfirstcleanarchitectureapp.framework.db.LocalSource
import com.kks.usecases.MovieUseCase
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.*
import com.kks.myfirstcleanarchitectureapp.*
import com.kks.myfirstcleanarchitectureapp.ui.common.DataState
import com.kks.myfirstcleanarchitectureapp.ui.common.ScreenState
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import com.kks.myfirstcleanarchitectureapp.ui.util.NetworkListener
import kotlinx.coroutines.runBlocking
import org.junit.Rule

import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    // region constants

    // endregion constants

    // region helper fields
    private lateinit var SUT: MainViewModel
    private val api = mock<Api>()
    private lateinit var db: AppDb
    private val networkUtil: NetworkUtilTestDouble = mock()
    private lateinit var remoteDataSource: RemoteSource
    private lateinit var localDataSource: LocalSource
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieUseCase: MovieUseCase
    // endregion helper fields

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDb::class.java)
            .allowMainThreadQueries().build()
        remoteDataSource = RemoteSource(api)
        localDataSource = LocalSource(db)
        movieRepository = MovieRepository(remoteDataSource, localDataSource)
        movieUseCase = MovieUseCase(movieRepository)
        SUT = MainViewModel(movieUseCase, networkUtil)

    }

    @Test
    fun testConfirmScreenStateIsLoading() {
        sleep()
        SUT.screenState.getOrAwaitValue().let {
            assertThat(it == ScreenState.Loading).isTrue()
        }
    }

    @Test
    fun testFetchMoviesFromRemote_Success_InvokeSuccess() {
        sleep()

        // Given
        whenever(networkUtil.isNetworkAvailable()).thenReturn(true)
        runBlocking {
            whenever(api.getMovies(any(), any(), any())).thenReturn(getMovieList(true))
        }

        // Act
        SUT.screenState
        SUT.screenState.observeForTesting {
            while (SUT.screenState.value == ScreenState.Loading) {
                // Do Nothing
            }
            val renderState = SUT.screenState.value
            if (renderState is ScreenState.Render) {
                val dataState = (SUT.screenState.value as ScreenState.Render<DataState>).renderState
                if (dataState is DataState.Success) {

                    // Verify
                    assertThat(dataState.data as List<*>).isNotEmpty()

                    val actual = (dataState.data as List<Movie>)[0].id
                    val expected = 1
                    assertThat(actual).isEqualTo(expected)
                } else {
                    throw Exception("${(dataState as DataState.Error).message}")
                }
            } else {
                throw Exception("$renderState")
            }
        }
    }

    @Test
    fun testFetchMovieFromRemote_Failure_InvokeError() {
        sleep()

        // Given
        whenever(networkUtil.isNetworkAvailable()).thenReturn(true)
        runBlocking {
            whenever(api.getMovies(any(), any(), any())).thenReturn(getMovieList(false))
        }

        // Act
        SUT.screenState
        SUT.screenState.observeForTesting {
            while (SUT.screenState.value == ScreenState.Loading) {
                // Do Nothing
            }
            val renderState = SUT.screenState.value
            if (renderState is ScreenState.Render) {
                val dataState = (SUT.screenState.value as ScreenState.Render<DataState>).renderState
                if (dataState is DataState.Error) {

                    // Verify
                    val actual = dataState.message
                    val expected = ERROR_MSG
                    assertThat(actual).isEqualTo(expected)
                } else {
                    throw Exception("${(dataState as DataState.Error).message}")
                }
            } else {
                throw Exception("$renderState")
            }
        }
    }

    @Test
    fun testFetchMovieFromRemote_Success_ReturnEmptyList_InvokeSuccess() {
        sleep(2)

        // Given
        whenever(networkUtil.isNetworkAvailable()).thenReturn(true)
        runBlocking {
            whenever(api.getMovies(any(), any(), any())).thenReturn(getEmptyMovieList())
        }

        // Act
        SUT.screenState
        SUT.screenState.observeForTesting {
            while (SUT.screenState.value == ScreenState.Loading) {
                // Do Nothing
            }
            val renderState = SUT.screenState.value
            if (renderState is ScreenState.Render) {
                val dataState = (SUT.screenState.value as ScreenState.Render<DataState>).renderState
                if (dataState is DataState.EndReach) {

                    // Verify
                    val expected = DataState.EndReach
                    assertThat(dataState).isEqualTo(expected)
                } else {
                    throw Exception("${(dataState as DataState.Error).message}")
                }
            } else {
                throw Exception("$renderState")
            }
        }
    }

    @Test
    fun testQueryMovieFromDb_Success_ReturnMovieList() {
        sleep()

        // Given
        whenever(networkUtil.isNetworkAvailable()).thenReturn(false)
        assertThat(networkUtil.isNetworkAvailable()).isFalse()

        db.MovieDao().insertMovies(getMovieList(true).results!!)

        // Act
        SUT.screenState
        SUT.screenState.observeForTesting {
            while (SUT.screenState.value == ScreenState.Loading) {
                // Do Nothing
            }
            val renderState = SUT.screenState.value
            if (renderState is ScreenState.Render) {
                val dataState = (SUT.screenState.value as ScreenState.Render<DataState>).renderState
                if (dataState is DataState.Success) {

                    // Verify
                    val actual = dataState.data as List<Movie>
                    val expected = getMovieList(true).results
                    assertThat(actual).isEqualTo(expected)
                } else {
                    throw Exception("${(dataState as DataState.Error).message}")
                }
            } else {
                throw Exception("$renderState")
            }
        }
    }

    @Test
    fun testQueryMovieFromDb_Success_ReturnEmptyList() {
        sleep()

        // Given
        whenever(networkUtil.isNetworkAvailable()).thenReturn(false)
        assertThat(networkUtil.isNetworkAvailable()).isFalse()

        // Act
        SUT.screenState // initialize livedata
        SUT.screenState.observeForTesting {
            while (SUT.screenState.value == ScreenState.Loading) {
                // Do Nothing
            }
            val renderState = SUT.screenState.value
            if (renderState is ScreenState.Render) {
                val dataState = (SUT.screenState.value as ScreenState.Render<DataState>).renderState
                if (dataState is DataState.EndReach) {

                    // Verify
                    val expected = DataState.EndReach
                    assertThat(dataState).isEqualTo(expected)
                } else {
                    throw Exception("${(dataState as DataState.Error).message}")
                }
            } else {
                throw Exception("$renderState")
            }
        }
    }

    // region helper methods

    // endregion helper methods

    // region helper classes
    open class NetworkUtilTestDouble : NetworkListener {
        override fun isNetworkAvailable(): Boolean {
            return true
        }

    }
    // endregion helper classes
}