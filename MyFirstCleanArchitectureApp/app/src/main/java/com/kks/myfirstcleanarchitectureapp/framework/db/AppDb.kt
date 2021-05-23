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
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "CAMovie"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
