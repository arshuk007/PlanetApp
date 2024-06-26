package com.test.planetapp.usecase

import com.test.planetapp.di.PlanetAppDao
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.network.Response
import com.test.planetapp.network.ResponseHandler
import com.test.planetapp.repository.HomeRepository

class HomeUsecase(private val responseHandler: ResponseHandler,
                  private val repository: HomeRepository,
                  private val planetAppDao: PlanetAppDao,) {

    suspend fun getPlanetList(): Resource<PlanetListResponse>{

        return try {
            when(val response = repository.getPlanetList()){
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

    suspend fun getPlanetListForNextPage(url: String?): Resource<PlanetListResponse>{

        return try {
            when(val response = repository.getPlanetListForNextPage(url)){
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

    fun savePlanetsToDB(planets: List<Planet>?): Boolean{

        planets?.forEachIndexed { index, planet ->
            planetAppDao.insertPlanet(planet)
        }
        return true

    }

    fun getPlanetsFromDB(): List<Planet>?{

        return planetAppDao.getAllPlanets()
    }

}