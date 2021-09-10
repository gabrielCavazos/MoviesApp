package com.example.moviestest

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie (
    @SerializedName("title") val title:String,
    @SerializedName("vote_count") val voteCount:Int,
    @SerializedName("average_vote") val averageVote:Double,
    @SerializedName("poster_path") val picture:String,
    @SerializedName("overview") val overview:String,
    @PrimaryKey(autoGenerate = true)
    var idMovie: Int = 0
)