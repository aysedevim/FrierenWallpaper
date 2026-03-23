package com.example.myapplication.wallpaper.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.wallpaper.core.navigation.AppNavHost
import com.example.myapplication.wallpaper.core.utils.Constants
import com.example.myapplication.wallpaper.data.remote.WallpaperNetworkProvider
import com.example.myapplication.wallpaper.data.repository.FavoriteRepositoryImpl
import com.example.myapplication.wallpaper.data.repository.WallpaperRepositoryImpl
import com.example.myapplication.wallpaper.data.shared.FavoriteShared
import com.example.myapplication.wallpaper.domain.model.BottomNavItem
import com.example.myapplication.wallpaper.domain.usecase.GetBannerUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetFavoritesUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetImageDetailUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostFavoritedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetMostViewedUseCase
import com.example.myapplication.wallpaper.domain.usecase.GetWallpapersPagingUseCase
import com.example.myapplication.wallpaper.domain.usecase.ToggleFavoriteUseCase
import com.example.myapplication.wallpaper.ui.theme.Primary
import com.example.myapplication.wallpaper.ui.theme.Purple
import com.example.myapplication.wallpaper.ui.theme.Purple40
import com.example.myapplication.wallpaper.ui.theme.ShoppingBookTheme
import com.example.myapplication.wallpaper.viewmodel.WallpaperViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val api = WallpaperNetworkProvider.api
        val wallpaperRepository = WallpaperRepositoryImpl(api)
        val favoriteRepository = FavoriteRepositoryImpl(FavoriteShared(this))

        val wallpaperViewModel = WallpaperViewModel(
            getWallpapersPagingUseCase = GetWallpapersPagingUseCase(wallpaperRepository),
            getMostViewedUseCase = GetMostViewedUseCase(wallpaperRepository),
            getMostFavoritedUseCase = GetMostFavoritedUseCase(wallpaperRepository),
            getBannerUseCase = GetBannerUseCase(wallpaperRepository),
            getImageDetailUseCase = GetImageDetailUseCase(wallpaperRepository),
            getFavoritesUseCase = GetFavoritesUseCase(favoriteRepository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(favoriteRepository)
        )

        setContent {
            ShoppingBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FrierenApp(viewModel= wallpaperViewModel)
                }
            }
        }
    }
}

@Composable
fun NavBottom(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = Primary,
        contentColor = Color.Black
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "icon",
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.titleResId),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple,
                    selectedTextColor = Purple40,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray
                )
            )
        }
    }
}

@Composable
fun FrierenApp(
    viewModel: WallpaperViewModel

) {
    val navController = rememberNavController()
    val constants = Constants()

    Scaffold(
        bottomBar = {
            NavBottom(
                items = constants.BottomNavItems,
                navController = navController,
                modifier = Modifier.height(80.dp)
            )
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}