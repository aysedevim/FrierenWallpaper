package com.example.myapplication.wallpaper.presentation.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.wallpaper.core.routes.AppRoute
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.data.mapper.toAppError
import com.example.myapplication.wallpaper.presentation.components.ErrorContent
import com.example.myapplication.wallpaper.presentation.viewmodel.BrowseScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.FavoritesViewModel
import com.example.myapplication.wallpaper.ui.theme.Primary
import com.example.myapplication.wallpaper.ui.theme.Purple
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.wallpaper.ui.theme.gradientColors


@Composable
fun BrowseScreen(
    navController: NavController,
    browseViewModel: BrowseScreenViewModel,
    favoriteViewModel: FavoritesViewModel
) {
    val wallpapersPagination = browseViewModel.wallpapersPagination.collectAsLazyPagingItems()
    val favoriteIds by favoriteViewModel.favoriteIds.collectAsState()

    LaunchedEffect(Unit) {
        browseViewModel.getWallpapers()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CategoryChips()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            DropDown()
        }

        PagingWallpaperGrid(
            wallpapersPagination = wallpapersPagination,
            favoriteIds = favoriteIds,
            onFavoriteClick = { wallpaperId ->
                favoriteViewModel.toggleFavorite(wallpaperId)
            },
            navController = navController
        )
    }
}

@Composable
fun DropDown() {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableStateOf(0)
    }

    val usernames = listOf(
        stringResource(R.string.popular),
        stringResource(R.string.newest),
        stringResource(R.string.most_liked),)

    Box {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDropDownExpanded.value = true
            }
        ) {
            Text(
                stringResource(R.string.sort_by),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Purple
            )
            Text(
                text = usernames[itemPosition.value],
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Purple
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint =  Purple
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = {
                isDropDownExpanded.value = false
            }) {
            usernames.forEachIndexed { index, username ->
                DropdownMenuItem(
                    text = {
                        Text(text = username)
                    },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemPosition.value = index
                    })
            }
        }
    }
}

@Composable
fun PagingWallpaperGrid(
    wallpapersPagination: LazyPagingItems<Wallpaper>,
    favoriteIds: List<String>,
    onFavoriteClick: (String) -> Unit,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = wallpapersPagination.itemCount,
            key = wallpapersPagination.itemKey { it.id },
            contentType = wallpapersPagination.itemContentType()
        ) { index ->
            val wallpaper = wallpapersPagination[index]
            wallpaper?.let {
                WallpaperCard(
                    wallpaper = it,
                    isFavorited = favoriteIds.contains(it.id),
                    onFavoriteClick = { onFavoriteClick(it.id) },
                    navController = navController
                )
            }
        }


        when (val refreshState = wallpapersPagination.loadState.refresh) {
            is LoadState.Loading -> {
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Purple,
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }
            }
            is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorContent(
                            error = refreshState.error.toAppError(),
                            onRetry = { wallpapersPagination.retry() }
                        )
                    }
                }
            }
            else -> {}
        }

        when (val appendState = wallpapersPagination.loadState.append) {
            is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Purple,
                            modifier = Modifier.size(48.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }
            }
            is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorContent(
                            error = appendState.error.toAppError(),
                            onRetry = { wallpapersPagination.retry() }
                        )
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .size(width = 120.dp, height = 250.dp)
            .clickable {
                navController.navigate(AppRoute.Detail.withId(wallpaper.id))
    }
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(8.dp)
        ) {
            AsyncImage(
                model = wallpaper.image_url,
                contentDescription = "Browse",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
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

@Composable
fun CategoryChips() {
    val categories = listOf(
        stringResource(R.string.trending),
        stringResource(R.string.new_releases),
        stringResource(R.string.quality_4k),
        stringResource(R.string.shonen),
        stringResource(R.string.minimalist),
        stringResource(R.string.cyberpunk)
    )


    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Column {
        Text(
            stringResource(R.string.browse),
            style = MaterialTheme.typography.titleMedium.copy(
                color= Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    onClick = { selectedCategory = category },
                    label = {
                        Text(
                            text = category,
                            color = if (selectedCategory == category) Color.White else Color.Black
                        )
                    },
                    selected = selectedCategory == category,
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.Transparent,
                        selectedLabelColor = Color.White,
                        containerColor = Color.LightGray,
                        labelColor = Color.Black
                    ),
                    modifier = Modifier
                        .then(
                            if (selectedCategory == category) {
                                Modifier.background(
                                    brush = Brush.horizontalGradient(colors = gradientColors),
                                    shape = RoundedCornerShape(20.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun CategoryChipsPreview() {
    CategoryChips()
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun DropDownPreview() {
    DropDown()
}
