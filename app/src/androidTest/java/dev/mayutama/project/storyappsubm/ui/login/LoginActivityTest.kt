package dev.mayutama.project.storyappsubm.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.data.remote.retrofit.ApiConfig
import dev.mayutama.project.storyappsubm.ui.main.MainActivity
import dev.mayutama.project.storyappsubm.util.EspressoIdlingResource
import dev.mayutama.project.storyappsubm.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    private val mockWebServer = MockWebServer()

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_success() {
        onView(withId(R.id.tv_welcome))
            .check(matches(isDisplayed()))

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_login_response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.edt_email))
            .perform(typeText("yudha@gmail.com"), closeSoftKeyboard())

        onView(withId(R.id.edt_password))
            .perform(typeText("12345678910"), closeSoftKeyboard())

        onView(withId(R.id.btn_login))
            .perform(click())
        intended(hasComponent(MainActivity::class.java.name))
    }
}