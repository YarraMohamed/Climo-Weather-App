package com.example.climo.favourites.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.climo.R
import com.example.climo.model.Alerts
import com.example.climo.model.Favourites
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.RobotoMedium

@Composable
fun FavouritesView() {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {},
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

            FavouritesList()
        }

    }

}

@Composable
private fun FavouritesList(){
    val favourites = listOf(
        Favourites("Cairo, Egypt",1.0,2.0),)
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center) {
        if(favourites.isNullOrEmpty()){
            CircularProgressIndicator(color = colorResource(R.color.blue))
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp),contentPadding = PaddingValues(bottom = 80.dp)){
            items(favourites.size) {
                FavouriteItem(favourites[it])
            }
        }
    }
}

@Composable
private fun FavouriteItem(favourite: Favourites){
    Card(
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.dark_blue)),
        modifier = Modifier
            .width(350.dp)
            .height(80.dp)){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
            Text(text=favourite.address,
                fontSize = 26.sp,
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
                    .padding(end = 20.dp)

            )
        }
    }
}


