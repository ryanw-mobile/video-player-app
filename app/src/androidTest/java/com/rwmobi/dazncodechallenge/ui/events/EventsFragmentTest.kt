/*
 * Copyright (c) 2021. Ryan Wong (hello@ryanwong.co.uk)
 *
 */

package com.rwmobi.dazncodechallenge.ui.events

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
internal class EventsFragmentTest {
//    @Inject
//    lateinit var repository: Repository
//
//    // HiltAndroidRule executes first - https://developer.android.com/training/dependency-injection/hilt-testing#multiple-testrules
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun repositoryEmpty_ShowNoData() =
//        runTest {
//            // GIVEN - Repository has no events to supply
//            (repository as FakeRepository).submitEventList(emptyList())
//            repository.refreshEvents()
//
//            // WHEN - Launching the fragment
//            // Note: Originally we use launchFragmentInContainer
//            // But due to library bugs, we use launchFragmentInHiltContainer
//            // See HiltExt.kt for details
//            launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)
//
//            // THEN - noDataTextView is shown
//            Espresso.onView(withId(R.id.textview_nodata))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        }
//
//    @Test
//    fun repositoryNonEmpty_HiddenNoData() =
//        runTest {
//            // GIVEN - Repository has 1 event
//            (repository as FakeRepository).submitEventList(listOf(event1))
//            repository.refreshEvents()
//
//            // WHEN - Launching the fragment
//            launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)
//
//            // THEN - noDataTextView is NOT shown
//            Espresso.onView(withId(R.id.textview_nodata))
//                .check(matches(not(isDisplayed())))
//        }
//
//    @Test
//    fun repositoryError_ShowErrorDialog() =
//        runTest {
//            // GIVEN - the repository is set to always return error
//            val errorMessage = "Instrumentation test error"
//            (repository as FakeRepository).setReturnError(true, errorMessage)
//
//            // WHEN - Launching the fragment
//            launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge)
//
//            // THEN - error dialog is shown
//            // Expects reminder to be saved successfully
//            Espresso.onView(withText(R.string.something_went_wrong))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        }
//
//    @Test
//    fun clickEvent_NavigateToExoplayerActivity() =
//        runTest {
//            // GIVEN - Load the Events Fragment with three events
//            (repository as FakeRepository).submitEventList(listOf(event1, event2, event3))
//            repository.refreshEvents()
//
//            // Note: Originally we use launchFragmentInContainer
//            // But due to library bugs, we use launchFragmentInHiltContainer
//            // See HiltExt.kt for details
//            val navController = Mockito.mock(NavController::class.java)
//            /***
//             * Warning: Hilt does not currently support FragmentScenario because there is no way to
//             * specify an activity class, and Hilt requires a Hilt fragment to be contained in a Hilt
//             * activity. One workaround for this is to launch a Hilt activity and then attach your fragment.
//             */
//            launchFragmentInHiltContainer<EventsFragment>(Bundle(), R.style.Theme_DaznCodeChallenge) {
//                Navigation.setViewNavController(requireView(), navController)
//            }
//
//            // WHEN - Click on the event1
//            Espresso.onView(withId(R.id.recyclerview))
//                // Using ViewMatchers to locate the item with event1.title on the RecyclerView
//                .perform(
//                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                        ViewMatchers.hasDescendant(ViewMatchers.withText(event1.title)),
//                        ViewActions.click(),
//                    ),
//                )
//
//            // THEN - Verify that we navigate to the player screen
//            Mockito.verify(navController).navigate(
//                EventsFragmentDirections.actionNavigationEventsToExoplayerActivity(event1.videoUrl),
//            )
//        }
}
