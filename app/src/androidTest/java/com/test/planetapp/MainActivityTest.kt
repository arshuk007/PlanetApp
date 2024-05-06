package com.test.planetapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.test.planetapp.activity.MainActivity
import org.junit.Rule
import org.junit.Test


class MainActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testHomeFragmentDisplayed() {
        // Check if the home fragment is displayed by checking the visibility of a view within it
        Espresso.onView(ViewMatchers.withId(R.id.container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testBackPressed() {
        // Simulate back press and check if the activity is finished
        Espresso.pressBack()
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            assert(activity.isFinishing)
        }
    }
}