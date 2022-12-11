package com.pierre.foodfacts.utils

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/**
 * Alternative way of waiting fo a view to be enabled to check something
 * Could be improved by using https://developer.android.com/training/testing/espresso/idling-resource
 */
fun onViewEnabled(viewMatcher: Matcher<View>): ViewInteraction {
    val isEnabled: () -> Boolean = {
        var isDisplayed = false
        isDisplayed = try {
            Espresso.onView(viewMatcher).check(ViewAssertions.matches((ViewMatchers.isEnabled())))
            true
        } catch (e: Exception) {
            false
        }
        isDisplayed
    }
    for (x in 0..20) {
        val isValid = isEnabled()
        Thread.sleep(500)
        if (isValid) {
            break
        }
    }
    return Espresso.onView(viewMatcher)
}