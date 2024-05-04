package com.test.planetapp.repository

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

    suspend fun getPlanetListForNextPage(url: String?): Response<PlanetListResponse>{
        val result = apiInterface.getPlanetListForNextPage(url)
        return with(result) {
            if (this.planets.isNullOrEmpty()) {
                Response.Failure(this.detail ?: "no data found")
            } else {
                Response.Success(this)
            }
        }
    }
}