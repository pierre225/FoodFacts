package com.pierre.foodfacts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pierre.foodfacts.utils.onViewEnabled
import com.pierre.ui.MainActivity
import com.pierre.ui.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Testing the UI of the SearchFragment with different states
 */
@RunWith(AndroidJUnit4::class)
class SearchFragmentTests {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun defaultInitialCard() {
        onView(withId(R.id.initialCard)).check(matches(isDisplayed()))
    }

    @Test
    fun onClickScanner() {
        onView(withId(R.id.scannerButton)).perform(ViewActions.click())

        onView(withId(R.id.initialCard)).check(matches(not(isDisplayed())))
        onView(withId(R.id.productCard)).check(matches(not(isDisplayed())))
        onView(withId(R.id.scannerCard)).check(matches(isDisplayed()))
    }

    @Test
    fun onSuccessSearch() {
        onView(withId(R.id.search)).perform(ViewActions.typeText("8076809514439"))

        onView(withId(R.id.baseLoader)).check(matches(isDisplayed()))
        onViewEnabled(withId(R.id.productCard)).check(matches(isDisplayed()))
    }

    @Test
    fun onWrongSearch() {
        onView(withId(R.id.search)).perform(ViewActions.typeText("000000000000"))

        onView(withId(R.id.initialCard)).check(matches(not(isDisplayed())))
        onView(withId(R.id.scannerCard)).check(matches(not(isDisplayed())))
        onViewEnabled(withId(R.id.initialCard)).check(matches(isDisplayed()))
    }
}