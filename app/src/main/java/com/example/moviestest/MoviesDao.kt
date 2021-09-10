package com.example.moviestest

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movie")
    fun getAll(): LiveData<List<Movie>>

    @Insert
    fun insert(vararg movies: Movie)

    @Query("DELETE FROM movie")
    fun deleteAll()

}