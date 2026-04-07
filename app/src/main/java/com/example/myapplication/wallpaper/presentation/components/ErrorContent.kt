package com.example.myapplication.wallpaper.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.wallpaper.domain.model.AppError
import com.example.myapplication.wallpaper.ui.theme.Purple

@Composable
fun ErrorContent(error: AppError, onRetry: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = when (error) {
                is AppError.Timeout -> "Bağlantı zaman aşımına uğradı"
                is AppError.Network -> "İnternet bağlantınızı kontrol edin"
                is AppError.NotFound -> "İçerik bulunamadı"
                is AppError.Unknown -> error.message ?: "Beklenmeyen bir hata oluştu"
            },
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        if (onRetry != null && (error is AppError.Timeout || error is AppError.Network)) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Tekrar Dene")
            }
        }
    }
}
