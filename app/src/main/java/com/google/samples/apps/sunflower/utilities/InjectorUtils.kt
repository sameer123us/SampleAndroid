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

package com.google.samples.apps.sunflower.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.samples.apps.sunflower.api.UnsplashService
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.data.UnsplashRepository
import com.google.samples.apps.sunflower.viewmodels.GalleryViewModelFactory
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModelFactory
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModelFactory
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getPlantRepository(context: Context): PlantRepository {
        return PlantRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).plantDao())
    }

    private fun getGardenPlantingRepository(context: Context): GardenPlantingRepository {
        return GardenPlantingRepository.getInstance(
                AppDatabase.getInstance(context.applicationContext).gardenPlantingDao())
    }

    fun provideGardenPlantingListViewModelFactory(
        context: Context
    ): GardenPlantingListViewModelFactory {
        return GardenPlantingListViewModelFactory(getGardenPlantingRepository(context))
    }

    fun providePlantListViewModelFactory(fragment: Fragment): PlantListViewModelFactory {
        return PlantListViewModelFactory(getPlantRepository(fragment.requireContext()), fragment)
    }

    fun providePlantDetailViewModelFactory(
        context: Context,
        plantId: String
    ): PlantDetailViewModelFactory {
        return PlantDetailViewModelFactory(getPlantRepository(context),
                getGardenPlantingRepository(context), plantId)
    }

    @ExperimentalCoroutinesApi
    fun provideGalleryViewModelFactory(): GalleryViewModelFactory {
        val repository = UnsplashRepository(UnsplashService.create())
        return GalleryViewModelFactory(repository)
    }
}
