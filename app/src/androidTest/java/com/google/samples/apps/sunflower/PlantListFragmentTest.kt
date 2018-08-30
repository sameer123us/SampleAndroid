/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.arch.lifecycle.ViewModelProviders
import android.support.test.annotation.UiThreadTest
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import androidx.navigation.findNavController
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantListFragmentTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(GardenActivity::class.java)
    private lateinit var viewModel: PlantListViewModel

    @Before
    @UiThreadTest
    fun openPlantListFragment() {
        activityTestRule.activity.apply {
            val factory = InjectorUtils.providePlantListViewModelFactory(applicationContext)
            viewModel = ViewModelProviders.of(this, factory).get(PlantListViewModel::class.java)
            findNavController(R.id.garden_nav_fragment).navigate(R.id.plant_list_fragment)
        }
    }

    @Test
    fun should_Show_LoadingUI_Before_Data_Loaded() {
        onView(withId(R.id.loading_ui)).check(matches(isDisplayed()))
    }

    @Test
    fun should_Dismiss_LoadingUI_After_Data_Loaded() {
        onView(withId(R.id.loading_ui)).check(matches(not(isDisplayed())))
    }
}