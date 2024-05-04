package com.test.planetapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.usecase.HomeUsecase
import com.test.planetapp.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var homeUsecase: HomeUsecase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = HomeViewModel(homeUsecase)
    }

    @Test
    fun `test getPlanetList`() {
        val mockResponse = Resource.success(PlanetListResponse(10,"","","",emptyList()))
        val observer = mockObserver<Resource<PlanetListResponse>>()

        coroutineTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(homeUsecase.getPlanetList()).thenReturn(mockResponse)

            viewModel.getPlanetList().observeForever(observer)

            Mockito.verify(observer).onChanged(mockResponse)
            viewModel.getPlanetList().removeObserver(observer)
        }
    }

    @Test
    fun `test getPlanetListForNextPage`() {
        val mockResponse = Resource.success(PlanetListResponse(10,"","","",emptyList()))
        val observer = mockObserver<Resource<PlanetListResponse>>()

        coroutineTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(homeUsecase.getPlanetListForNextPage("some_url")).thenReturn(mockResponse)

            viewModel.getPlanetListForNextPage("some_url").observeForever(observer)

            Mockito.verify(observer).onChanged(mockResponse)
            viewModel.getPlanetListForNextPage("some_url").removeObserver(observer)
        }
    }

    @After
    fun tearDown() {
        // Clean up any resources if needed
    }

    // Helper function to create a mocked observer
    private fun <T> mockObserver(): Observer<T> = Mockito.mock(Observer::class.java) as Observer<T>
}