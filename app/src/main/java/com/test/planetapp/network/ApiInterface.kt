package com.test.planetapp.network

import com.test.planetapp.model.PlanetListResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET("planets/")
    suspend fun getPlanetList(): PlanetListResponse
}