package com.test.planetapp
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.test.planetapp.databinding.FragmentHomeBinding
import com.test.planetapp.fragment.HomeFragment
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.utils.showToast
import com.test.planetapp.viewmodel.HomeViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeFragmentTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockViewModel: HomeViewModel

    @Mock
    private lateinit var mockBinding: FragmentHomeBinding

    @Mock
    private lateinit var mockObserver: Observer<PlanetListResponse>

    private lateinit var homeFragment: HomeFragment

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        homeFragment = HomeFragment()
        homeFragment.binding = mockBinding
    }

    @Test
    fun `init should call getPlanetDetails`() {
        homeFragment.init()
        verify(mockViewModel).getPlanetList()
    }

    @Test
    fun `handleSuccessForFirstPage should update adapter with planets`() {
        val mockData: PlanetListResponse = mock()
        val mockPlanets: List<Planet> = listOf(mock(), mock())
        whenever(mockData.planets).thenReturn(mockPlanets)

        homeFragment.handleSuccessForFirstPage(mockData)

        verify(homeFragment.adapter)?.clear()
        verify(homeFragment.adapter)?.addAll(mockPlanets)
    }

    @Test
    fun `handleSuccessForFirstPage should set isLastPage to true if data is null or empty`() {
        homeFragment.handleSuccessForFirstPage(null)
        assertEquals(true, homeFragment.isLastPage)

        homeFragment.handleSuccessForFirstPage(PlanetListResponse(10, "","","",emptyList()))
        assertEquals(true, homeFragment.isLastPage)
    }

    @Test
    fun `handleSuccessForFirstPage should set isLastPage to false if data is not empty`() {
        val mockData: PlanetListResponse = mock()
        whenever(mockData.planets).thenReturn(listOf(mock()))

        homeFragment.handleSuccessForFirstPage(mockData)
        assertEquals(false, homeFragment.isLastPage)
    }

    @Test
    fun `handleSuccessForFirstPage should set nextGetURL if data has next`() {
        val mockData: PlanetListResponse = mock()
        whenever(mockData.planets).thenReturn(listOf(mock()))
        whenever(mockData.next).thenReturn("nextURL")

        homeFragment.handleSuccessForFirstPage(mockData)
        assertEquals("nextURL", homeFragment.nextGetURL)
    }

    @Test
    fun `handleFail should show toast message`() {
        val mockMessage = "Error message"
        homeFragment.handleFail(mockMessage)
        verify(mockMessage).showToast(any())
    }

    @Test
    fun `loadMoreItems should call getPlanetListForNextPage`() {
        homeFragment.loadMoreItems()
        verify(mockViewModel).getPlanetListForNextPage(anyOrNull())
    }
}