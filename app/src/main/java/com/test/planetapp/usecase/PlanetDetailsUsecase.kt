package com.test.planetapp.usecase

import com.test.planetapp.model.FilmResponse
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.model.ResidentResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.network.Response
import com.test.planetapp.network.ResponseHandler
import com.test.planetapp.repository.PlanetDetailsRepository

class PlanetDetailsUsecase(private val responseHandler: ResponseHandler,
                           private val repository: PlanetDetailsRepository) {

    suspend fun getResidentDetails(url: String?): Resource<ResidentResponse?>{

        return try {
            when(val response = repository.getResidentDetails(url)){
                is Response.Success -> {
                    responseHandler.handleSuccess(response.data)
                }
                is Response.Failure -> {
                    responseHandler.handleFail(response.message)
                }
            }
        }catch (e: Exception){
            responseHandler.handleFail(e.localizedMessage ?:"")
        }

    }

    suspend fun getFilmDetails(url: String?): Resource<FilmResponse?>{

        return try {
            when(val response = repository.getFilmDetails(url)){
                is Response.Success -> {
                    responseHandler.handleSuccess(response.data)
                }
                is Response.Failure -> {
                    responseHandler.handleFail(response.message)
                }
            }
        }catch (e: Exception){
            responseHandler.handleFail(e.localizedMessage ?:"")
        }

    }

}