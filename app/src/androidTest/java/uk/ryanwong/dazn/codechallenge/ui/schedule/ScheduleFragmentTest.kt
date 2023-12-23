/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package uk.ryanwong.dazn.codechallenge.ui.schedule

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import uk.ryanwong.dazn.codechallenge.R
import uk.ryanwong.dazn.codechallenge.TestData.schedule1
import uk.ryanwong.dazn.codechallenge.TestData.schedule2
import uk.ryanwong.dazn.codechallenge.data.repository.FakeRepository
import uk.ryanwong.dazn.codechallenge.data.repository.Repository
import uk.ryanwong.dazn.codechallenge.launchFragmentInHiltContainer
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
internal class ScheduleFragmentTest {
    @Inject
    lateinit var repository: Repository

    // HiltAndroidRule executes first - https://developer.android.com/training/dependency-injection/hilt-testing#multiple-testrules
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun repositoryEmpty_showNoData() =
        runTest {
            // GIVEN - Repository has no events to supply
            (repository as FakeRepository).submitScheduleList(emptyList())
            repository.refreshSchedule()

            // WHEN - Launching the fragment
            // Note: Originally we use launchFragmentInContainer
            // But due to library bugs, we use launchFragmentInHiltContainer
            // See HiltExt.kt for details
            launchFragmentInHiltContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

            // THEN - noDataTextView is shown
            Espresso.onView(withId(R.id.textview_nodata))
                .check(matches(isDisplayed()))
        }

    @Test
    fun repositoryNonEmpty_HiddenNoData() =
        runTest {
            // GIVEN - Repository has 1 schedule to supply
            (repository as FakeRepository).submitScheduleList(listOf(schedule1))
            repository.refreshSchedule()

            // WHEN - Launching the fragment
            // Note: Originally we use launchFragmentInHiltContainer
            // But due to library bugs, we use launchFragmentInHiltContainer
            // See HiltExt.kt for details
            launchFragmentInHiltContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

            // THEN - noDataTextView is NOT shown
            Espresso.onView(withId(R.id.textview_nodata))
                .check(matches(Matchers.not(isDisplayed())))
        }

    @Test
    fun repositoryError_ShowErrorDialog() =
        runTest {
            // GIVEN - the repository is set to always return error
            val errorMessage = "Instrumentation test error"
            (repository as FakeRepository).setReturnError(true, errorMessage)

            // WHEN - Launching the fragment
            launchFragmentInHiltContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)

            // THEN - error dialog is shown
            // Expects reminder to be saved successfully
            Espresso.onView(withText(R.string.something_went_wrong))
                .check(matches(isDisplayed()))
        }

    @Test
    fun wait31Seconds_ScheduleRefreshed() =
        runTest {
            (repository as FakeRepository).submitScheduleList(listOf(schedule1))
            // repository.refreshSchedule()

            // GIVEN - On the Schedule List screen we have schedule1
            launchFragmentInHiltContainer<ScheduleFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)
            Espresso.onView(withText(schedule1.title)).check(matches(isDisplayed()))

            // WHEN - Repository returns a new schedule list after 31 seconds
            (repository as FakeRepository).submitScheduleList(listOf(schedule2))
            // repository.refreshSchedule()
            Thread.sleep(35000)

            // THEN - RecyclerView should show schedule2
            Espresso.onView(withText(schedule2.title)).check(matches(isDisplayed()))
        }
}
