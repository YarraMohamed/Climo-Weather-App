package com.example.climo.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.climo.R
import com.example.climo.model.WeatherStatus
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.InterBold
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.InterMedium
import com.example.climo.view.ui.theme.InterSemiBold
import com.example.climo.view.ui.theme.RobotoBold
import com.example.climo.view.ui.theme.RobotoRegular

@Preview(showSystemUi = true)
@Composable
fun HomeView(){
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        WeatherDetails(WeatherStatus("13°C","Clear Sky","Cairo, Egypt"))

        //Hourly Details
        Text(text= stringResource(R.string.hourly_forecast),
            color = colorResource(R.color.white),
            fontSize = 32.sp,
            fontFamily = InterExtraBold,
            modifier = Modifier.padding(top=25.dp, start = 20.dp))
        HourlyDetails()

        //Weather Details
        Text(text= stringResource(R.string.daily_details),
            color = colorResource(R.color.white),
            fontSize = 32.sp,
            fontFamily = InterExtraBold,
            modifier = Modifier.padding(top=20.dp, start = 20.dp))
        WeatherConditions()

        //Daily Forecast
        Text(text= stringResource(R.string.daily_forecast),
            color = colorResource(R.color.white),
            fontSize = 32.sp,
            fontFamily = InterExtraBold,
            modifier = Modifier.padding(top=20.dp, start = 20.dp))
        DailyForecast()
    }
}


@Composable
private fun WeatherDetails(weatherStatus: WeatherStatus) {
    Column (modifier = Modifier
        .fillMaxWidth()){
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp)){
            Image(
                painter = painterResource(R.drawable.date_icon),
                contentDescription = stringResource(R.string.date_icon),
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 2.dp))
            Text(text="April, 12",
                color = colorResource(R.color.white),
                fontSize = 20.sp,
                fontFamily = RobotoBold,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .align(alignment = Alignment.CenterVertically))
            Image(
                painter = painterResource(R.drawable.clock_icon),
                contentDescription = stringResource(R.string.clock_icon),
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 5.dp, end = 2.dp))
            Text(text="1:30 PM",
                color = colorResource(R.color.white),
                fontSize = 20.sp,
                fontFamily = RobotoBold,
                modifier = Modifier
                    .padding(end = 15.dp)
                    .align(alignment = Alignment.CenterVertically))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp)) {
            Column(modifier = Modifier
                .weight(2f)
                .padding(top = 30.dp)) {
                Text(text = weatherStatus.weather,
                    fontSize = 58.sp,
                    color = colorResource(R.color.white),
                    fontFamily = InterBold
                    )
                Row(modifier = Modifier.padding(top=10.dp)){
                    Image(
                        painter = painterResource(R.drawable.location_icon2),
                        contentDescription = stringResource(R.string.location_icon),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                    Text(text = weatherStatus.address,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontFamily = RobotoRegular
                    )
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(top = 20.dp)){
                Image(
                    painter = painterResource(R.drawable.weather_photo),
                    contentDescription = stringResource(R.string.condition_photo),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(0.dp)
                )
                Text(text = weatherStatus.condition,
                    fontSize = 20.sp,
                    color = colorResource(R.color.white),
                    fontFamily = RobotoRegular
                )
            }
        }
    }

}

@Composable
private fun HourlyDetails() {
    val list = listOf("day1","day2","day3","day4")
    Box(modifier = Modifier.padding(top=18.dp,start=10.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            items(list.size){
                HourlyDetailsItem()
            }
        }
    }
}

@Composable
private fun HourlyDetailsItem() {
    Card (shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.dark_blue)),
        modifier = Modifier
            .width(180.dp)
            .height(130.dp)) {
        Row{
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp)
                .weight(1f)
                .align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
                Text(text="2:00",
                    color = colorResource(R.color.white),
                    fontSize = 22.sp,
                    fontFamily = RobotoRegular
                )
                Text(
                    text = stringResource(R.string.pm),
                    color = colorResource(R.color.white),
                    fontSize = 22.sp,
                    fontFamily = RobotoRegular
                )
            }
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(R.drawable.condition_photo),
                    contentDescription = stringResource(R.string.condition_photo),
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = "17°C",
                    color = colorResource(R.color.white),
                    fontSize = 32.sp,
                    modifier = Modifier.padding(top=5.dp),
                    fontFamily = InterBold
                )
            }
        }

    }
}

@Composable
private fun WeatherConditions() {
    Card (shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = colorResource(R.color.grey)),
        modifier = Modifier
            .width(380.dp)
            .height(180.dp)
            .fillMaxSize()
            .padding(top = 15.dp, start = 20.dp)){

        Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 15.dp, top = 15.dp, bottom = 20.dp)) {
                Image(
                    painter = painterResource(R.drawable.wind_speed_photo),
                    contentDescription = stringResource(R.string.wind_speed_photo),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 5.dp)
                )
                Column(modifier = Modifier.padding(start = 5.dp,end=50.dp)) {
                    Text(text = stringResource(R.string.wind_speed),
                        color = colorResource(R.color.black),
                        fontSize = 16.sp,
                        fontFamily = InterMedium
                    )
                    Text(text = "1.4 m/s",
                        color = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = RobotoRegular
                    )
                }
                Image(
                    painter = painterResource(R.drawable.clouds_photo),
                    contentDescription = stringResource(R.string.clouds_photo),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 5.dp)
                )
                Column(modifier = Modifier.padding(start = 5.dp,end=50.dp)) {
                    Text(
                        text = stringResource(R.string.clouds),
                        color = colorResource(R.color.black),
                        fontSize = 16.sp,
                        fontFamily = InterMedium
                    )
                    Text(text = "100%",
                        color = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = RobotoRegular
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 15.dp, top = 10.dp, bottom = 20.dp)) {
                Image(
                    painter = painterResource(R.drawable.humidity_photo),
                    contentDescription = stringResource(R.string.humidity_photo),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 5.dp)
                )
                Column(modifier = Modifier.padding(start = 5.dp,end=70.dp)) {
                    Text(
                        text = stringResource(R.string.humidity),
                        color = colorResource(R.color.black),
                        fontSize = 16.sp,
                        fontFamily = InterMedium
                    )
                    Text(text = "96%",
                        color = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = RobotoRegular
                    )
                }
                Image(
                    painter = painterResource(R.drawable.pressure_photo),
                    contentDescription = stringResource(R.string.pressure_photo),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 5.dp)
                )
                Column(modifier = Modifier.padding(start = 5.dp,end=50.dp)) {
                    Text(
                        text = stringResource(R.string.pressure),
                        color = colorResource(R.color.black),
                        fontSize = 16.sp,
                        fontFamily = InterMedium
                    )
                    Text(text = "100%",
                        color = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = RobotoRegular
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyForecast(){
    val list = listOf("day1","day2","day3","day4","day5","day6","day7")
        Card (shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = colorResource(R.color.dark_blue)),
            modifier = Modifier
                .padding(top=20.dp,start=20.dp, bottom = 20.dp)
                .width(370.dp).height(500.dp)){
            LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize(unbounded = false)) {
                items(list.size){
                    DailyForecastDetails()
                }

            }
        }


}

@Composable
private fun DailyForecastDetails(){
    Row(modifier = Modifier.padding(start=30.dp,top=15.dp, bottom = 15.dp).fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Sun",
                color = colorResource(R.color.white),
                fontSize = 24.sp,
               fontFamily = InterSemiBold
            )
            Text(text = "April, 14",
                color = colorResource(R.color.white),
                fontSize = 24.sp,
                fontFamily = InterSemiBold
            )
        }
        Image(
            painter = painterResource(R.drawable.condition_photo),
            contentDescription = stringResource(R.string.condition_photo),
            modifier = Modifier.size(50.dp).weight(1f)
        )
        Text(text = "15°C",
            color = colorResource(R.color.white),
            fontSize = 28.sp,
            modifier = Modifier.weight(1f),
            fontFamily = InterSemiBold
        )

    }
}