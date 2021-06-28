package com.kks.myfirstcleanarchitectureapp.ui.mvvm.view.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.kks.myfirstcleanarchitectureapp.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    // region constants

    // endregion constants

    // region helper fields
    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)
    // endregion helper fields

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = scenarioRule.scenario
    }

    @Test
    fun testMainActivity() {
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//        sleep()
//        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    // region helper methods
    private fun sleep(second : Int = 1) {
        Thread.sleep(second * 1000L)
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes
}