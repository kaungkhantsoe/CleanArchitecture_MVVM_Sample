package com.kks.myfirstcleanarchitectureapp.framework.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kks.myfirstcleanarchitectureapp.*
import com.kks.myfirstcleanarchitectureapp.framework.db.dao.MovieDao
import com.kks.domain.MovieRequest as DomainMovie
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.toDomainModel
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class LocalSourceTest {

    // region constants

    // endregion constants

    // region helper fields

    private lateinit var SUT: LocalSource
    private lateinit var db: AppDb
    private val movieDao: MovieDao = mock()

    private val movieListAc : KArgumentCaptor<List<DomainMovie>> = argumentCaptor()
    // endregion helper fields


    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDb::class.java)
            .allowMainThreadQueries().build()

        SUT = LocalSource(db)
    }

    @Test
    fun getMovieList_Success_ReturnDomainMovieList() {
        // Prepare
        db.MovieDao().insertMovies(getMovieList(true).results!!)

        // Act
        val actual = SUT.getMovieListFor(PAGE_ONE)

        // Verify
        val expected = getMovieList(true).results!!.map {
            it.toDomainModel()
        }
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getMovieList_Success_ReturnEmptyList() {
        // Prepare
        db.MovieDao().insertMovies(getMovieList(true).results!!)

        // Act
        val actual = SUT.getMovieListFor(PAGE_TWO)

        // Verify
        val expected = listOf<DomainMovie>()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getMovieWithId_Success_ReturnMovie() {
        // Given
        db.MovieDao().insertMovies(getMovieList(true).results!!)

        // Act
        val actual = SUT.getMovieWith(MOVIE_ID_ONE)
        val expected = getMovieList(true).results!!
            .filter { it.id == MOVIE_ID_ONE }
            .map { it.toDomainModel() }[0]

        // Verify
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getMovieWithId_Failure_ReturnNull() {
        // Given
        db.MovieDao().insertMovies(getMovieList(true).results!!)

        // Act
        val actual = SUT.getMovieWith(MOVIE_ID_MINUS)

        // Verify
        assertThat(actual).isNull()
    }

    @Test
    fun insertMovieList_Success_VerifyListIsStored() {
        // Given
        val domainMovieList = getDomainMovieList(true).results!!

        // Act
        SUT.insertMovieList(domainMovieList)

        // Verify
        val actual = SUT.getMovieListFor(PAGE_ONE)
        val expected = getDomainMovieList(true).results!!
        assertThat(actual).isEqualTo(expected)

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