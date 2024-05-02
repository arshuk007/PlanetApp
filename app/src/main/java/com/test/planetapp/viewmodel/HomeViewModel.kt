package com.test.planetapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.usecase.HomeUsecase

class HomeViewModel(private val usecase: HomeUsecase): ViewModel() {

    fun getPlanetList(): LiveData<Resource<PlanetListResponse>> = liveData {
        emit(usecase.getPlanetList())
    }

    fun getPlanetListForNextPage(url: String?): LiveData<Resource<PlanetListResponse>> = liveData {
        emit(usecase.getPlanetListForNextPage(url))
    }

}