package com.test.planetapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.test.planetapp.adapter.PlanetListAdapter
import com.test.planetapp.fragment.HomeFragment
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import com.test.planetapp.network.Resource
import com.test.planetapp.network.Response
import com.test.planetapp.network.Status
import com.test.planetapp.utils.CommonUtils
import com.test.planetapp.viewmodel.HomeViewModel
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class HomeFragmentTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var observer: Observer<Resource<PlanetListResponse>>

    private lateinit var fragment: HomeFragment

    @Before
    fun setUp() {
        fragment = HomeFragment()
    }

    @Test
    fun `test handleSuccessForFirstPage`() {
        val data = PlanetListResponse(10, "", "", "", emptyList())
        val resource = Resource(Status.SUCCESS, data, null)
        val result = viewModel.getPlanetList()
        Assert.assertNotNull(result)

//        fragment.handleSuccessForFirstPage(data)
//
//        assertEquals(fragment.adapter?.itemCount, 0)
//        assertEquals(fragment.isLastPage, true)
//        assertEquals(fragment.nextGetURL, null)
    }
//
//    @Test
//    fun `test handleSuccessForSecondPage`() {
//        val initialData = listOf(Planet("Earth"))
//        fragment.planets = ArrayList(initialData)
//        fragment.adapter = PlanetListAdapter(fragment.requireContext())
//
//        val newData = listOf(Planet("Mars"))
//        val data = PlanetListResponse(newData, "")
//        val resource = Resource(Status.SUCCESS, data, null)
//        fragment.handleSuccessForSecondPage(data)
//
//        assertEquals(fragment.adapter?.itemCount, initialData.size + newData.size)
//        assertEquals(fragment.isLastPage, true)
//        assertEquals(fragment.nextGetURL, null)
//    }
//
//    @Test
//    fun `test getPlanetDetails with success`() {
//        `when`(CommonUtils.isNetworkAvailable(fragment.requireContext())).thenReturn(true)
//        val data = PlanetListResponse(emptyList(), "")
//        val resource = Resource(Status.SUCCESS, data, null)
//        `when`(viewModel.getPlanetList()).thenReturn(resource)
//
//        fragment.getPlanetDetails()
//
//        verify(viewModel).getPlanetList()
//        verify(observer).onChanged(resource)
//    }

}