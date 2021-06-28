package com.kks.myfirstcleanarchitectureapp.framework.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.*
import com.kks.myfirstcleanarchitectureapp.framework.db.dao.MovieDao
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDbTest : TestCase() {

    // region constants

    // endregion constants

    // region helper fields

    // endregion helper fields

    private lateinit var SUT: AppDb
    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        SUT = Room.inMemoryDatabaseBuilder(context,AppDb::class.java).build()

        movieDao = SUT.MovieDao()

    }

    @Test
    fun writeAndReadMovies() {
        // Prepare
        val movie = Movie(1,"title","poster_path","overview",1)
        val movieList = listOf(movie)

        // Act
        movieDao.insertMovies(movieList)

        // Verify
        assertThat(movieDao.getMoviesFrom(1).contains(movie)).isTrue()
    }

    @Test
    fun writeAndDeleteMovies() {
        // Prepare
        val movie = Movie(1,"title","poster_path","overview",1)
        val movieList = listOf(movie)

        // Act
        movieDao.insertMovies(movieList)
        movieDao.deleteMovies()

        // Verify
        assertThat(movieDao.getMoviesFrom(1).isEmpty()).isTrue()
    }

    @After
    fun closeDb() {
        SUT.close()
    }

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}