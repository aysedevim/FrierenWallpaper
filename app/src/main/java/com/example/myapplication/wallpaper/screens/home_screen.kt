@file:OptIn(ExperimentalFoundationApi::class)

package com.example.myapplication.wallpaper.screens

import com.example.myapplication.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import coil.compose.AsyncImage
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.ui.theme.LightBlue
import com.example.myapplication.wallpaper.ui.theme.Primary
import com.example.myapplication.wallpaper.ui.theme.Purple
import com.example.myapplication.wallpaper.ui.theme.gradientColors
import com.example.myapplication.wallpaper.viewmodel.WallpaperViewModel


@Composable
fun HomeScreen(navController: NavController,
               viewModel: WallpaperViewModel = viewModel(),

) {
    val bannerImage by viewModel.bannerImage
    val mostViewedWallpapers = viewModel.mostViewedWallpapers.collectAsLazyPagingItems()
    val mostFavoritedWallpapers = viewModel.mostFavoritedWallpapers.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.loadBanner()
        viewModel.getMostViewed()
        viewModel.getMostFavorited()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Primary)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ABox()
            Spacer(Modifier.size(8.dp))
            GradientText(stringResource(id = R.string.anime_walls))
        }

        BannerCard(
            wallpaper = bannerImage,
            onBannerClick = {

            }
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleText(stringResource(id = R.string.most_viewed))
            ButtonText(
                onClick = {
                    navController.navigate("browse")
                }
            )
        }

        WallpaperCarousel(
            pagingItems = mostViewedWallpapers,
            navController = navController,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleText(stringResource(id = R.string.most_favorited))
            ButtonText(
                onClick = {
                    navController.navigate("browse")
                }
            )

        }
        WallpaperCarousel(
            pagingItems = mostFavoritedWallpapers,
            navController=navController
        )

    }
}

@Composable
fun ABox() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "A",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun GradientText(text: String) {

    Text(
        text = text,
        style = TextStyle(
            brush = Brush.linearGradient(colors = gradientColors),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun BannerCard(
    wallpaper: Wallpaper?,
    onBannerClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f)
            .clickable(enabled = wallpaper != null) { onBannerClick() }
    ) {
        if (wallpaper != null) {
            AsyncImage(
                model = wallpaper.image_url,
                contentDescription = "Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun TitleText(text: String) {

    Text(
        text = text,
        style = TextStyle(
            color = Color.Gray,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ButtonText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            (stringResource(R.string.see_all)),
            style = TextStyle(
                color = Purple,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
        )
    }
}


@Composable
fun WallpaperCarousel(
    pagingItems: LazyPagingItems<Wallpaper>,
    navController: NavController,
) {
    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Purple,
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 3.dp
                )
            }
        }

        else -> {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentPadding = PaddingValues(start = 0.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    contentType = pagingItems.itemContentType()
                ) { index ->
                    val wallpaper = pagingItems[index]
                    wallpaper?.let {
                        CarouselItem(
                            wallpaper = it,
                            navController= navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselItem(
    wallpaper: Wallpaper,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .height(240.dp)
            .width(160.dp)
            .clickable {  navController.navigate("detail/${wallpaper.id}") },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = wallpaper.image_url,
                contentDescription = "Wallpaper",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Views",
                        tint = LightBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = wallpaper.view_count.toString(),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = Purple,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = wallpaper.favorite_count.toString(),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}


