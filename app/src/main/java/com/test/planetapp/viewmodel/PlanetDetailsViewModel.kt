package com.test.planetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.test.planetapp.model.FilmResponse
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.model.ResidentResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.usecase.PlanetDetailsUsecase

class PlanetDetailsViewModel(private val usecase: PlanetDetailsUsecase): ViewModel() {

    fun getResidentDetails(url: String?): LiveData<Resource<ResidentResponse?>> = liveData {
        emit(usecase.getResidentDetails(url))
    }

    fun getFilmDetails(url: String?): LiveData<Resource<FilmResponse?>> = liveData {
        emit(usecase.getFilmDetails(url))
    }

}