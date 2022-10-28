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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.samples.apps.sunflower.databinding.HomeViewPagerFragmentBinding
import com.google.samples.apps.sunflower.databinding.PlantDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GardenActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Displaying edge-to-edge
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "home") {
        composable("home") {
          HomeViewPagerScreen(supportFragmentManager) { plantId ->
            navController.navigate("plant/$plantId")
          }
        }
        composable("plant/{plantId}",
          arguments = listOf(navArgument("plantId") {
            type = NavType.StringType
          })
        ) { backStackEntry ->
          val plantId = backStackEntry.arguments?.getString("plantId")
          PlantDetailScreen(supportFragmentManager, plantId)
        }
      }
    }
  }
}

@Composable
fun PlantDetailScreen(supportFragmentManager: FragmentManager, plantId: String?) {
  AndroidViewBinding(factory = PlantDetailFragmentBinding::inflate) {
    supportFragmentManager.commit {
      val bundle = bundleOf("plantId" to plantId)
      add<PlantDetailFragment>(R.id.plant_detail_fragment_container, args = bundle)
    }
  }
}

@Composable
fun HomeViewPagerScreen(
  supportFragmentManager: FragmentManager,
  onPlantClicked: (String) -> Unit
) {
  val lifecycle = LocalLifecycleOwner.current
  AndroidViewBinding(factory = { inflater, parent, attachToParent ->
    supportFragmentManager.setFragmentResultListener("plantDetailRequestKey", lifecycle) { _, bundle ->
      bundle.getString("plantId")?.let {
        onPlantClicked(it)
      }
    }
    HomeViewPagerFragmentBinding.inflate(inflater, parent, attachToParent)
  })
}