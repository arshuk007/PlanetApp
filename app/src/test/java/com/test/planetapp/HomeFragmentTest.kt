package com.test.planetapp

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.test.planetapp.fragment.HomeFragment
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.usecase.HomeUsecase
import com.test.planetapp.viewmodel.HomeViewModel
import org.junit.Before
import org.junit.Test

class HomeFragmentTest {

    private lateinit var fragment: HomeFragment
    private lateinit var viewModel: HomeViewModel
    private lateinit var observer: Observer<Resource<PlanetListResponse>>

    private val planet: Planet = mock()
    private val context: Context = mock()
    private val mockUsecase: HomeUsecase = mock()

    @Before
    fun setUp() {
        fragment = HomeFragment()
        viewModel = HomeViewModel(mockUsecase)
        observer = mock()
    }

    @Test
    fun testGetPlanetDetails() {

        // Mock response
        val mockResponse = Resource.success(PlanetListResponse(10, "", "", "", emptyList()))
        whenever(viewModel.getPlanetList()).thenReturn(liveData { emit(mockResponse) })

        // Invoke method under test
        fragment.getPlanetDetails()

        // Verify interactions
        verify(fragment.parentActivity).showLoading()
        verify(fragment.parentActivity).dismissLoading()
        verify(fragment.adapter)?.clear()
        verify(fragment.adapter)?.addAll(mockResponse.data?.planets!!)
        verify(fragment).savePlanetsToDB(mockResponse.data?.planets!!)
    }

    @Test
    fun testSetAdapter() {
        // Mock data
        val mockPlanets = listOf(planet)

        // Invoke method under test
        fragment.setAdapter(mockPlanets)

        // Verify adapter is called with the correct data
        verify(fragment.adapter)?.addAll(mockPlanets)
    }

    @Test
    fun testLoadMoreItems() {
        // Invoke method under test
        fragment.loadMoreItems()

        // Verify getPlanetListForNextPage is called
        verify(fragment).getPlanetListForNextPage()
    }

    @Test
    fun testGetPlanetListForNextPage () {
        // Mock response
        val mockResponse = Resource.success(PlanetListResponse(10, "", "", "", emptyList()))
        whenever(viewModel.getPlanetListForNextPage(fragment.nextGetURL)).thenReturn(liveData { emit(mockResponse) })

        // Invoke method under test
        fragment.getPlanetListForNextPage()

        // Verify handleSuccessForSecondPage is called with the correct data
        verify(fragment).handleSuccessForSecondPage(mockResponse.data)
    }

}