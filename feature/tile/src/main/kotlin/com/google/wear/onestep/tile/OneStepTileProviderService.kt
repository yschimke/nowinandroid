/*
 * Copyright 2021 Google LLC
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.onestep.tile

import android.util.Log
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders.Tile
import com.google.android.horologist.tiles.SuspendingTileService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneStepTileProviderService : SuspendingTileService() {
    private lateinit var renderer: OneStepTileRenderer

    override fun onCreate() {
        super.onCreate()

        renderer = OneStepTileRenderer(this)
    }

    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        return renderer.produceRequestedResources(
            resourceState = Unit,
            requestParams = requestParams
        )
    }

    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): Tile {
        Log.i("WhereAmI", "tileRequest $requestParams")

        return renderer.renderTimeline(Any(), requestParams)
    }
}
