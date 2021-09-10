package com.example.moviestest

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("title") val title:String,
    @SerializedName("vote_count") val voteCount:Int,
    @SerializedName("average_vote") val averageVote:Double,
    @SerializedName("poster_path") val picture:String,
    @SerializedName("overview") val overview:String,
)