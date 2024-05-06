package com.test.planetapp
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.planetapp.fragment.HomeFragment
import com.test.planetapp.model.Planet
import com.test.planetapp.model.PlanetListResponse
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentUITest {

    @Test
    fun testRecyclerViewDisplayedWithData() {
        // Given
        val scenario = launchFragmentInContainer<HomeFragment>()
        val planets = listOf(Planet("url", "name","","",
            "","","","","",
            "", "","", emptyList(), emptyList()), Planet("url", "name2","","",
            "","","","","",
            "", "","", emptyList(), emptyList()))
        val planetListResponse = PlanetListResponse(3, "","","",planets)

        // Delay for 5 seconds
        Thread.sleep(10000)

        // When
        scenario.onFragment { fragment ->
            fragment.handleSuccessForFirstPage(planetListResponse)
        }

        // Then
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testNoInternetMessageDisplayedWhenNetworkUnavailable() {
        // Given
        val scenario = launchFragmentInContainer<HomeFragment>()

        // When
        scenario.onFragment { fragment ->
            fragment.handleFail("No internet connection")
        }

        // Then
        onView(withText(R.string.no_internet)).check(matches(isDisplayed()))
    }

}