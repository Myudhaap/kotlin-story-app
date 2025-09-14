package dev.mayutama.project.storyappsubm.ui.main.setting

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.ui.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingFragmentTest {

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun logout_success() {
        val scenario = launchFragmentInContainer<SettingFragment>(themeResId = R.style.Theme_StoryAppSubm)

        scenario.onFragment { fragment ->
            val navController = TestNavHostController(fragment.requireContext())
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.nav_setting)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.logout))
            .perform(click())
        intended(hasComponent(LoginActivity::class.java.name))

        onView(withId(R.id.tv_welcome))
            .check(matches(isDisplayed()))
    }
}