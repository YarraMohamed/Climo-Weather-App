package com.example.climo.favourites.view

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.climo.R
import com.example.climo.favourites.viewmodel.FavouritesViewModel
import com.example.climo.model.Alerts
import com.example.climo.model.Favourites
import com.example.climo.model.Response
import com.example.climo.utilities.ErrorAnimation
import com.example.climo.view.NavigationRoutes
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.OutfitBold
import com.example.climo.view.ui.theme.RobotoMedium

@Composable
fun FavouritesView(viewModel : FavouritesViewModel, navController: NavHostController) {
    val context = LocalContext.current
    var favouritesState = viewModel.favList.collectAsStateWithLifecycle()
    var messageState = viewModel.message.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(messageState.value) {
        if(!messageState.value.isNullOrBlank())
            snackBarHostState.showSnackbar(messageState.value!!, duration = SnackbarDuration.Short)
    }

    when(favouritesState.value){
        is Response.Failure -> {
            ErrorAnimation()
            Toast.makeText(context,"Error in getting data",Toast.LENGTH_SHORT).show()
        }
        Response.Loading -> {
            Row(horizontalArrangement = Arrangement.Center){
                CircularProgressIndicator(color = colorResource(R.color.white))
            }
        }
        is Response.Success -> {
            Scaffold (
                snackbarHost = { SnackbarHost(snackBarHostState) },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(NavigationRoutes.FavMap)
                    },
                        containerColor = colorResource(R.color.white),
                        modifier = Modifier.padding(16.dp)){
                        Image(
                            painter = painterResource(R.drawable.add_icon),
                            contentDescription = stringResource(R.string.add_icon),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                containerColor = Color.Transparent
            ){ paddingValues ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                ) {

                    Text(text= stringResource(R.string.favourites),
                        color = colorResource(R.color.white),
                        fontSize = 32.sp,
                        fontFamily = InterExtraBold,
                        modifier = Modifier.padding(start = 20.dp))
                    FavouritesList((favouritesState.value as Response.Success).data,viewModel,navController)
                }
            }

        }
    }
}

@Composable
private fun FavouritesList(favourites: List<Favourites>,viewModel: FavouritesViewModel,navController: NavHostController){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center) {
        if(favourites.isNullOrEmpty()){
            MapAnimation()
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp),contentPadding = PaddingValues(bottom = 80.dp)){
            items(favourites.size) {
                FavouriteItem(
                    favourite = favourites[it],
                    {viewModel.deleteFav(favourites[it])},
                    { lat,lon -> navController.navigate(NavigationRoutes.Home(lat,lon))})
            }
        }
    }
}

@Composable
private fun FavouriteItem(favourite: Favourites, action:()->Unit, nav : (lat:Double,lon:Double)->Unit){
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.dark_blue)),
        modifier = Modifier
            .width(350.dp)
            .height(80.dp)
            .clickable(onClick = { nav(favourite.latitude, favourite.longitude) })){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
            Text(text=favourite.address,
                fontSize = 22.sp,
                color = colorResource(R.color.white),
                fontFamily = RobotoMedium,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(R.drawable.bin_icon),
                contentDescription = stringResource(R.string.bin_icon),
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 5.dp, end = 20.dp)
                    .clickable(onClick = {
                        AlertDialog.Builder(context)
                            .setTitle("Delete Item")
                            .setMessage("Are you sure you want to delete this item?")
                            .setPositiveButton("Delete") { _, _ ->
                                 action()
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    })
            )
        }
    }
}

@Composable
private fun MapAnimation(){
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.map_animation)
    )

    val lottieProgress by animateLottieCompositionAsState(
        lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = lottieProgress,
            modifier = Modifier.size(300.dp)
        )
    }

}


