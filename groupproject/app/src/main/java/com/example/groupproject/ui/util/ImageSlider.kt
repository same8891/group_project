// Add this to your build.gradle file
// implementation "io.coil-kt:coil-compose:1.4.0"

package com.yisheng.shoppingapplication.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.size.SizeResolver
import coil.size.ViewSizeResolver
import coil.transform.Transformation
import coil.util.CoilUtils
import kotlinx.coroutines.launch

@Composable
fun ImageSlider(imageUrls: List<String>, modifier: Modifier = Modifier) {
    var currentPage by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .wrapContentSize()
            .background(Color.Gray)
    ) {
        // ViewPager
        ViewPager(
            count = imageUrls.size,
            modifier = Modifier
                .wrapContentSize()
                .background(Color.White),
            onPageChanged = { page ->
                currentPage = page
            }
        ) {
            // Image
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageUrls[it]).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            )

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            PageIndicator(
                pageCount = imageUrls.size,
                currentPage = currentPage,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager(
    count: Int,
    modifier: Modifier = Modifier,
    onPageChanged: (Int) -> Unit,
    content: @Composable (page: Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        count
    })

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        content(page)
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (index == currentPage) Color.White else Color.Gray,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            )
        }
    }
}
