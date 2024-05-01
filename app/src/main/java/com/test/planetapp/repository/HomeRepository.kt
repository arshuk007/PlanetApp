package com.test.planetapp.repository

import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.ApiInterface
import com.test.planetapp.network.Response

class HomeRepository(private val apiInterface: ApiInterface) {

    suspend fun getPlanetList(): Response<PlanetListResponse>{
        val result = apiInterface.getPlanetList()
        return with(result) {
            if (this.planets.isNullOrEmpty()) {
                Response.Failure(this.detail ?: "no data found")
            } else {
                Response.Success(this)
            }
        }
    }
}