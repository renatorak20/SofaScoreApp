package com.example.sofascoreapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.sofascoreapp.data.model.Favourite
import com.example.sofascoreapp.data.model.RecentSearch

@Dao
interface SofascoreDao {

    @Query("SELECT * FROM recentsTable")
    suspend fun getAllRecents(): List<RecentSearch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecent(recent: RecentSearch)

    @Query("DELETE FROM recentsTable WHERE id = :id")
    suspend fun deleteRecent(id: Int)

    @Query("DELETE FROM recentsTable")
    suspend fun nukeRecents()

    @Update
    suspend fun updateRecents(favs: List<RecentSearch>)
}