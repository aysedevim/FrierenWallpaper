package com.example.myapplication.wallpaper.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem (
    val titleResId: Int,
    val icon: ImageVector,
    val route: String,
    )