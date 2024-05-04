package com.test.planetapp.model

import com.google.gson.annotations.SerializedName

data class FilmResponse(
    @SerializedName("title")
    val title: String?,

    @SerializedName("opening_crawl")
    val openingCrawl: String?,

    @SerializedName("release_date")
    val releaseDate: String?

)