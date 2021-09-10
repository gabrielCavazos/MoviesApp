package com.example.moviestest

import com.google.gson.annotations.SerializedName

data class MoviesResponse (
    @SerializedName("results") val movies:List<Movie>,
)