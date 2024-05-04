package com.test.planetapp.repository

import com.test.planetapp.model.FilmResponse
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.model.ResidentResponse
import com.test.planetapp.network.ApiInterface
import com.test.planetapp.network.Response

class PlanetDetailsRepository(private val apiInterface: ApiInterface) {

    suspend fun getResidentDetails(url: String?): Response<ResidentResponse?>{
        val result = apiInterface.getResidentDetails(url)
        return with(result) {
            if (this != null ) {
                Response.Success(this)
            } else {
                Response.Failure("no data found")
            }
        }
    }

    suspend fun getFilmDetails(url: String?): Response<FilmResponse?>{
        val result = apiInterface.getFilmDetails(url)
        return with(result) {
            if (this != null ) {
                Response.Success(this)
            } else {
                Response.Failure("no data found")
            }
        }
    }
}