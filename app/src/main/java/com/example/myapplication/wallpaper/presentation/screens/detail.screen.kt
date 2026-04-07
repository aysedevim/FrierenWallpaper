package com.example.myapplication.wallpaper.presentation.screens
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.wallpaper.domain.model.Wallpaper
import com.example.myapplication.wallpaper.presentation.components.ErrorContent
import com.example.myapplication.wallpaper.domain.model.WallpaperDestination
import com.example.myapplication.wallpaper.presentation.viewmodel.DetailScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.FavoritesViewModel
import com.example.myapplication.wallpaper.ui.theme.LightBlue
import com.example.myapplication.wallpaper.ui.theme.Primary
import com.example.myapplication.wallpaper.ui.theme.Purple
import com.example.myapplication.wallpaper.ui.theme.gradientColors
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    wallpaperId: String,
    detailViewModel: DetailScreenViewModel,
    favoriteViewModel: FavoritesViewModel,
) {
    val state by detailViewModel.state.collectAsState()
    val favoriteIds by favoriteViewModel.favoriteIds.collectAsState()
    val context = LocalContext.current

    val isFavorited = favoriteIds.contains(wallpaperId)

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(wallpaperId) {
        detailViewModel.loadImageDetail(wallpaperId)
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Purple,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorContent(
                        error = state.error!!,
                        onRetry = { detailViewModel.loadImageDetail(wallpaperId) }
                    )
                }
            }
            state.wallpaper != null -> {
                val wallpaper = state.wallpaper!!

                DetailImage(wallpaper = wallpaper)

                DetailTopNavBar(
                    isFavorited = isFavorited,
                    onBackClick = { navController.navigateUp() },
                    onFavoriteClick = {
                        favoriteViewModel.toggleFavorite(wallpaperId)
                    },
                )

                BottomCard(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    isFavorited = isFavorited,
                    onFavoriteClick = { favoriteViewModel.toggleFavorite(wallpaperId) },
                    onSetWallpaperClick = { showBottomSheet = true },
                    viewCount = wallpaper.view_count,
                    favoriteCount = wallpaper.favorite_count
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = Primary,
        ) {
            SetWallpaperBottomSheet(
                onOptionSelected = { destination ->
                    detailViewModel.setWallpaper(destination)  { result ->
                            result.onSuccess {
                                Toast.makeText(
                                    context,
                                    "Wallpaper set successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }.onFailure { error ->
                                Toast.makeText(
                                    context,
                                    "Failed to set wallpaper: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                        onDismiss = { showBottomSheet = false } )
                }

        }
}


@Composable
fun SetWallpaperBottomSheet(
    onOptionSelected: (WallpaperDestination) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Set as Wallpaper",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        WallpaperOptionButton(
            onClick = { onOptionSelected(WallpaperDestination.HOME) },
            title =  stringResource(R.string.home_screen),
            description =  stringResource(R.string.set_as_home),
        )

        Spacer(modifier = Modifier.height(12.dp))

        WallpaperOptionButton(
            onClick = { onOptionSelected(WallpaperDestination.LOCK) },
            title =  stringResource(R.string.lock_screen),
            description =  stringResource(R.string.set_as_lock),
        )

        Spacer(modifier = Modifier.height(12.dp))

        WallpaperOptionButton(
            onClick = { onOptionSelected(WallpaperDestination.BOTH) },
            title = stringResource(R.string.both_screens),
            description =  stringResource(R.string.set_as_both),
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.cancel),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun WallpaperOptionButton(
    onClick: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun DetailImage(wallpaper: Wallpaper) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        AsyncImage(
            model = wallpaper.image_url,
            contentDescription = "Wallpaper",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DetailTopNavBar(
    isFavorited: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlassIconButton(
            onClick = onBackClick,
            icon = Icons.Default.ArrowBack
        )

        GlassIconButton(
            onClick = onFavoriteClick,
            icon = if (isFavorited) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
            isActive = isFavorited
        )
    }
}

@Composable
fun BottomCard(
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit,
    onSetWallpaperClick: () -> Unit,
    viewCount: Int,
    favoriteCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
            .background(
                color = Primary,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .padding(horizontal = 24.dp)
    ) {
        SetWallpaperButton(
            isFavorited = isFavorited,
            onFavoriteClick = onFavoriteClick,
            onSetWallpaperClick = onSetWallpaperClick
        )

        ViewDetailsButton(
            onClick = {},
        )

        Spacer(Modifier.size(16.dp))

        Dividers(
            viewCount = viewCount,
            favoriteCount = favoriteCount
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Title(stringResource(R.string.related_wallpapers))
            SeeAllButton(onClick = {})
        }
    }
}

@Composable
fun GlassIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    isActive: Boolean = false
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) Purple else Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SetWallpaperButton(
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit,
    onSetWallpaperClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GradientButton(
            onClick = onSetWallpaperClick,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        GlassIconButton(
            onClick = onFavoriteClick,
            icon = if (isFavorited) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
            isActive = isFavorited
        )
    }
}

@Composable
fun GradientButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Wallpaper,
                contentDescription = stringResource(R.string.set_as_wallpaper),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Text(
                stringResource(R.string.set_as_wallpaper),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun ViewDetailsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color = MaterialTheme.colorScheme.onSurface)
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.view_details),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontWeight = FontWeight.Bold,
        ),
        modifier = Modifier.padding(vertical = 12.dp),
    )
}

@Composable
fun SeeAllButton(onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() }
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
fun Dividers(
    viewCount: Int,
    favoriteCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Visibility,
                    contentDescription = null,
                    tint = LightBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = viewCount.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.views),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            VerticalDivider(
                color = MaterialTheme.colorScheme.secondary,
                thickness = 1.dp,
                modifier = Modifier
                    .height(30.dp)
                    .align(Alignment.CenterVertically)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Purple,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = favoriteCount.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    stringResource(R.string.favorites_count),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 1.dp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BottomCardPreview() {
    BottomCard(
        isFavorited = true,
        onFavoriteClick = {},
        onSetWallpaperClick = {},
        viewCount = 1250,
        favoriteCount = 340
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun SetWallpaperBottomSheetPreview() {
    SetWallpaperBottomSheet(
        onOptionSelected = {},
        onDismiss = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GradientButtonPreview() {
    GradientButton(onClick = {})
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun DividersPreview() {
    Dividers(viewCount = 500, favoriteCount = 120)
}

