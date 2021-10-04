package com.kks.myfirstcleanarchitectureapp.framework.data

import com.kks.myfirstcleanarchitectureapp.*
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toDomainModel
import com.kks.domain.MovieListRequest as DomainMovieList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class RemoteSourceTest {

    // region constants

    // endregion constants

    // region helper fields
    private lateinit var SUT: RemoteSource
    private val mApi: Api = mock()
    // endregion helper fields

    @Before
    fun setup() {
        SUT = RemoteSource(mApi)
    }

    @Test
    fun `Fetch movie list with page and return movie list on success`(): Unit =
        runBlocking {
            // Given
            whenever(mApi.getMovies(any(), any(), eq(PAGE_ONE))).thenReturn(getMovieList())

            // Act
            val result: DomainMovieList = SUT.requestMovieListFor(PAGE_ONE)

            // Verify
            verify(mApi).getMovies(any(), any(), eq(PAGE_ONE))
            assertEquals(result,getMovieList().toDomainModel())
        }

    @Test
    fun `Fetch movie list with page less than 1 and return errors on failure`(): Unit =
        runBlocking {
            // Given
            whenever(mApi.getMovies(any(), any(), eq(-1))).thenReturn(getMovieList(false))

            // Act
            val result: DomainMovieList = SUT.requestMovieListFor(-1)

            // Verify
            verify(mApi).getMovies(any(), any(), eq(-1))
            assertEquals(result,getDomainMovieList(false))
        }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}