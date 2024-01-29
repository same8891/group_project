package com.example.groupproject.ui.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException

@Composable
fun SvgImage(svgData: ByteArray, modifier: Modifier = Modifier) {
    // Convert SVG to Bitmap
    val bitmap = try {
        val svg = SVG.getFromString(String(svgData))
        Bitmap.createBitmap(svg.documentWidth.toInt(), svg.documentHeight.toInt(), Bitmap.Config.ARGB_8888).also {
            val canvas = Canvas(it)
            svg.renderToCanvas(canvas)
        }
    } catch (e: IOException) {
        // Handle IOException
        null
    }

    // Check if the bitmap is not null before displaying
    if (bitmap != null) {
        val imageBitmap = bitmap.asImageBitmap()

        // Display the Image
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = modifier
        )
    }
}