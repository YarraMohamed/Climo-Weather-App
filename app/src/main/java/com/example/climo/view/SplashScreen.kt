package com.example.climo.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.climo.R
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.OutfitBold
import kotlinx.coroutines.delay

@Composable
fun SplashScreenView(navController: NavHostController) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animation)
    )

    val lottieProgress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        modifier = Modifier.fillMaxSize().background(GradientBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = lottieProgress,
            modifier = Modifier.size(300.dp)
        )

        Text(
            text = stringResource(R.string.climo),
            fontSize = 48.sp,
            color = colorResource(R.color.white),
            fontFamily = OutfitBold
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(NavigationRoutes.Home) {
            popUpTo(NavigationRoutes.Splash) { inclusive = true }
        }
    }
}
