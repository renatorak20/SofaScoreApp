package com.example.sofascoreapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sofascoreapp.data.model.Favourite
import com.example.sofascoreapp.data.model.RecentSearch

@Database(
    entities = [RecentSearch::class, Favourite::class],
    version = 2,
    exportSchema = false
)
abstract class SofascoreApiDatabase : RoomDatabase() {

    abstract fun sofascoreDao(): SofascoreDao

    companion object {
        private var dbInstance: SofascoreApiDatabase? = null

        fun getDatabase(context: Context): SofascoreApiDatabase? {
            if (dbInstance == null) {
                dbInstance = buildDatabase(context)
            }
            return dbInstance
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SofascoreApiDatabase::class.java,
            "SofascoreApiDatabase.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

}