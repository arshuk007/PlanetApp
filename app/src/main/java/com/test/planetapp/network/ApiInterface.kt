package com.test.planetapp.network

import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.model.ResidentResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    @GET("planets/")
    suspend fun getPlanetList(): PlanetListResponse

    @GET
    suspend fun getPlanetListForNextPage(@Url nextUrl: String?): PlanetListResponse

    @GET
    suspend fun getResidentDetails(@Url apiUrl: String?): ResidentResponse?

}