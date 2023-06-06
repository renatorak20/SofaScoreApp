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

    @Query("SELECT * FROM favouritesTable")
    suspend fun getAllFavourites(): List<Favourite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecent(recent: RecentSearch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(recent: Favourite)

    @Query("DELETE FROM recentsTable WHERE id = :id")
    suspend fun deleteRecent(id: Int)

    @Query("DELETE FROM favouritesTable WHERE id = :id")
    suspend fun deleteFavourite(id: Int)
}