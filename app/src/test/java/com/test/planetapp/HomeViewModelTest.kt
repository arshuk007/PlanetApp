package com.test.planetapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.usecase.HomeUsecase
import com.test.planetapp.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val mockUsecase: HomeUsecase = mock()
    private val planet: Planet = mock()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        viewModel = HomeViewModel(mockUsecase)
    }

    @Test
    fun testGetPlanetList() = testDispatcher.runBlockingTest {
        Dispatchers.setMain(StandardTestDispatcher())

        // Mock response
        val mockResponse = Resource.success(PlanetListResponse(10, "", "", "", emptyList()))
        `when`(mockUsecase.getPlanetList()).thenReturn(mockResponse)

        // Observer
        val observer: Observer<Resource<PlanetListResponse>> = mock()

        // Observe LiveData from viewModel
        viewModel.getPlanetList().observeForever(observer)

        // Assert
        assertEquals(mockResponse, viewModel.getPlanetList().value)
    }

    @Test
    fun testGetPlanetListForNextPage() = testDispatcher.runBlockingTest {
        // Mock response
        Dispatchers.setMain(StandardTestDispatcher())
        val mockResponse = Resource.success(PlanetListResponse(10, "", "", "", emptyList()))
        `when`(mockUsecase.getPlanetListForNextPage("next_page")).thenReturn(mockResponse)

        // Observer
        val observer: Observer<Resource<PlanetListResponse>> = mock()

        // Observe LiveData from viewModel
        viewModel.getPlanetListForNextPage("next_page").observeForever(observer)

        // Assert
        assertEquals(mockResponse, viewModel.getPlanetListForNextPage("next_page").value)
    }

    @Test
    fun testSavePlanetsToDB() = testDispatcher.runBlockingTest {
        // Mock response
        Dispatchers.setMain(StandardTestDispatcher())
        val mockResponse = true
        `when`(mockUsecase.savePlanetsToDB(listOf(planet))).thenReturn(mockResponse)

        // Observer
        val observer: Observer<Boolean> = mock()

        // Observe LiveData from viewModel
        viewModel.savePlanetsToDB(listOf(planet)).observeForever(observer)

        // Assert
        assertEquals(mockResponse, viewModel.savePlanetsToDB(listOf(planet)).value)
    }

    @Test
    fun testGetPlanetsFromDB() = testDispatcher.runBlockingTest {
        // Mock response
        Dispatchers.setMain(StandardTestDispatcher())
        val mockResponse = listOf(planet)
        `when`(mockUsecase.getPlanetsFromDB()).thenReturn(mockResponse)

        // Observer
        val observer: Observer<List<Planet>?> = mock()

        // Observe LiveData from viewModel
        viewModel.getPlanetsFromDB().observeForever(observer)

        // Assert
        assertEquals(mockResponse, viewModel.getPlanetsFromDB().value)
    }

}