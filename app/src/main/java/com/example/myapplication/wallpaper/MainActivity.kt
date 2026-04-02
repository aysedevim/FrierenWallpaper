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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.wallpaper.core.navigation.AppNavHost
import com.example.myapplication.wallpaper.core.utils.Constants
import com.example.myapplication.wallpaper.domain.model.BottomNavItem
import com.example.myapplication.wallpaper.ui.theme.Primary
import com.example.myapplication.wallpaper.ui.theme.Purple
import com.example.myapplication.wallpaper.ui.theme.Purple40
import com.example.myapplication.wallpaper.ui.theme.ShoppingBookTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.BrowseScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.DetailScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.FavoritesViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.HomeScreenViewModel
import com.example.myapplication.wallpaper.presentation.viewmodel.PagingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ShoppingBookTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FrierenApp()
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
fun FrierenApp( isPreview: Boolean = false) {

    val navController = rememberNavController()
    val constants = Constants()

    val favoriteViewModel: FavoritesViewModel = hiltViewModel()
    val browseViewModel: BrowseScreenViewModel = hiltViewModel()
    val pagingViewModel: PagingViewModel = hiltViewModel()
    val homeViewModel: HomeScreenViewModel = hiltViewModel()
    val detailViewModel: DetailScreenViewModel = hiltViewModel()


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
            favoriteViewModel = favoriteViewModel,
            browseViewModel = browseViewModel,
            homeViewModel = homeViewModel,
            detailViewModel = detailViewModel,
            pagingViewModel = pagingViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
