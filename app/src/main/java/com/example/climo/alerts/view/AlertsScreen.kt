package com.example.climo.alerts.view

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
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.climo.R
import com.example.climo.model.Alerts
import com.example.climo.model.Favourites
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.RobotoMedium

@Composable
fun AlertView() {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {},
                containerColor = colorResource(R.color.white),
                modifier = Modifier.padding(16.dp)){
                Image(
                    painter = painterResource(R.drawable.notification_icon),
                    contentDescription = stringResource(R.string.notification_icon),
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

            Text(text= stringResource(R.string.alerts),
                color = colorResource(R.color.white),
                fontSize = 32.sp,
                fontFamily = InterExtraBold,
                modifier = Modifier.padding(start = 20.dp))

            AlertsList()
        }
    }

}

@Composable
private fun AlertsList(){
    val alerts = listOf(
        Alerts("Cairo","1:00 PM","2:00 PM"),
        Alerts("Cairo","1:00 PM","2:00 PM"),
        Alerts("Cairo","1:00 PM","2:00 PM"),
        Alerts("Cairo","1:00 PM","2:00 PM"),
        Alerts("Cairo","1:00 PM","2:00 PM"),
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center) {
        if(alerts.isNullOrEmpty()){
            CircularProgressIndicator(color = colorResource(R.color.blue))
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp),contentPadding = PaddingValues(bottom = 80.dp)){
            items(alerts.size) {
                AlertItem(alerts[it])
            }
        }
    }
}


@Composable
private fun AlertItem(alerts: Alerts){
    Card(
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.dark_blue)
        ),
        modifier = Modifier
            .width(350.dp)
            .height(80.dp)){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
            Text(text="From\n${alerts.startTime}",
                fontSize = 18.sp,
                color = colorResource(R.color.white),
                fontFamily = RobotoMedium,
                modifier = Modifier
                    .padding(start = 15.dp)
                    .align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(R.drawable.arrow_icon),
                contentDescription = stringResource(R.string.arrow_icon),
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 13.dp, end = 13.dp)
            )
            Text(text="To\n${alerts.endTime}",
                fontSize = 18.sp,
                color = colorResource(R.color.white),
                fontFamily = RobotoMedium,
                modifier = Modifier
                    .padding(end = 25.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(text=alerts.address,
                fontSize = 22.sp,
                color = colorResource(R.color.white),
                fontFamily = RobotoMedium,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.CenterVertically)
            )

            Image(
                painter = painterResource(R.drawable.alert_icon),
                contentDescription = stringResource(R.string.alert_icon),
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 10.dp)

            )
        }
    }
}
