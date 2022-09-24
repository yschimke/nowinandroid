package com.google.wear.ads.tile

import android.content.Context
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.LayoutElementBuilders.Box
import androidx.wear.tiles.LayoutElementBuilders.Column
import androidx.wear.tiles.LayoutElementBuilders.HORIZONTAL_ALIGN_CENTER
import androidx.wear.tiles.LayoutElementBuilders.VERTICAL_ALIGN_CENTER
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.material.Colors
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.google.wear.ads.compose.theme.AdsColorPalette

class AdsTileRenderer(context: Context) :
    SingleTileLayoutRenderer<Any, Unit>(context) {
    override fun createTheme(): Colors {
        return AdsColorPalette.toTileColors()
    }

    override fun renderTile(
        state: Any,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
    ): LayoutElementBuilders.LayoutElement {
        return Box.Builder()
            .setWidth(DimensionBuilders.expand())
            .setHeight(DimensionBuilders.expand())
            .setVerticalAlignment(VERTICAL_ALIGN_CENTER)
            .addContent(
                Column.Builder()
                    .setHorizontalAlignment(HORIZONTAL_ALIGN_CENTER)
                    .setWidth(DimensionBuilders.expand())
                    .apply {

                    }.build()
            ).build()
    }

    override fun Resources.Builder.produceRequestedResources(
        resourceState: Unit,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>,
    ) {
    }
}
