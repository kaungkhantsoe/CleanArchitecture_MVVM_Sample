package com.kks.myfirstcleanarchitectureapp.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kks.myfirstcleanarchitectureapp.framework.db.dao.MovieDao
import com.kks.myfirstcleanarchitectureapp.ui.mvvm.model.Movie

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
@Database(
    entities = [Movie::class],
    exportSchema = false,
    version = 1
)
abstract class AppDb : RoomDatabase() {
    // define dao
    abstract fun MovieDao(): MovieDao

    companion object {
        var DB_NAME = "CAMovie.DB"
        var instance: AppDb? = null
        fun getInstance(context: Context?): AppDb? {
            return if (instance != null) {
                instance
            } else Room.databaseBuilder(context!!, AppDb::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}
