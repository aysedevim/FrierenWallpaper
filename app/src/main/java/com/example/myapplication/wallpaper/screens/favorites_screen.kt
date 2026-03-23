package com.example.myapplication.wallpaper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.ui.theme.Primary
import com.example.myapplication.wallpaper.ui.theme.Purple
import com.example.myapplication.wallpaper.viewmodel.WallpaperViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: WallpaperViewModel = viewModel(),

) {
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val allWallpapers = viewModel.wallpapersPagination.collectAsLazyPagingItems()

    val favoritedList = FavoriteList(allWallpapers, favoriteIds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        Text(
            text = stringResource(R.string.favorites),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        when (allWallpapers.loadState.refresh) {
            is LoadState.Loading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
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
                if (favoriteIds.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_favorites_yet),
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    val favoritedList = FavoriteList(allWallpapers, favoriteIds)

                    FavoriteWallpaperGrid(
                        wallpapers = favoritedList,
                        favoriteIds = favoriteIds,
                        onFavoriteClick = { wallpaperId ->
                            viewModel.toggleFavorite(wallpaperId)
                        },
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteList(
    allWallpapers: androidx.paging.compose.LazyPagingItems<Wallpaper>,
    favoriteIds: List<String>
): List<Wallpaper> {
    return remember(favoriteIds, allWallpapers.itemCount) {
        val list = mutableListOf<Wallpaper>()
        for (i in 0 until allWallpapers.itemCount) {
            val wallpaper = allWallpapers[i]
            if (wallpaper != null && favoriteIds.contains(wallpaper.id)) {
                list.add(wallpaper)
            }
        }
        list
    }
}


@Composable
fun FavoriteWallpaperGrid(
    wallpapers: List<Wallpaper>,
    favoriteIds: List<String>,
    onFavoriteClick: (String) -> Unit,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(wallpapers) { wallpaper ->
            FavoriteWallpaperCard(
                wallpaper = wallpaper,
                isFavorited = favoriteIds.contains(wallpaper.id),
                onFavoriteClick = { onFavoriteClick(wallpaper.id) },
                navController = navController
            )
        }
    }
}

@Composable
fun FavoriteWallpaperCard(
    wallpaper: Wallpaper,
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .size(width = 120.dp, height = 250.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController.navigate("detail/${wallpaper.id}")
                }

        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = wallpaper.image_url,
                    contentDescription = "Favorite Wallpaper",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            GlassIconButton(
                onClick = onFavoriteClick,
                icon = if (isFavorited) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                isActive = isFavorited
            )

        }
    }
}
