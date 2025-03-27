package com.example.climo.home.view

import android.location.Geocoder
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.climo.R
import com.example.climo.home.viewmodel.HomeViewModel
import com.example.climo.model.CurrentWeather
import com.example.climo.model.Deatils
import com.example.climo.model.Response
import com.example.climo.utilities.formatDate
import com.example.climo.utilities.formatTime
import com.example.climo.view.ui.theme.InterBold
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.InterMedium
import com.example.climo.view.ui.theme.InterSemiBold
import com.example.climo.view.ui.theme.RobotoBold
import com.example.climo.view.ui.theme.RobotoRegular
import kotlinx.coroutines.launch

@Composable
fun HomeView(viewModel: HomeViewModel){
    val context = LocalContext.current
    val weatherStatus = viewModel.currentWeather.collectAsStateWithLifecycle()
    val weatherHourlyForecastStatus = viewModel.currentWeatherForecast.collectAsStateWithLifecycle()
    val weatherDailyForecastStatus = viewModel.currentDailyForecast.collectAsStateWithLifecycle()
    val timeStatus = viewModel.time.collectAsStateWithLifecycle()
    val dateStatus = viewModel.date.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    viewModel.getCurrentTime()
    viewModel.getCurrentDate()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        when(weatherStatus.value){
            is Response.Loading -> {
                CircularProgressIndicator(color = colorResource(R.color.white), modifier = Modifier
                    .align(Alignment.CenterHorizontally))
            }
            is Response.Success -> {
                val address = viewModel.getAddressFromLocation(
                    (weatherStatus.value as Response.Success).data.coord.lat
                    ,(weatherStatus.value as Response.Success).data.coord.lon
                    ,Geocoder(context))
                WeatherDetails((weatherStatus.value  as Response.Success).data,address,timeStatus.value,dateStatus.value)
                //Hourly Details
                Text(text= stringResource(R.string.hourly_forecast),
                    color = colorResource(R.color.white),
                    fontSize = 32.sp,
                    fontFamily = InterExtraBold,
                    modifier = Modifier.padding(top=25.dp, start = 20.dp))
                when(weatherHourlyForecastStatus.value){
                    is Response.Failure -> {
                        Toast.makeText(context,(weatherHourlyForecastStatus.value as Response.Failure).err.message,Toast.LENGTH_SHORT).show()
                    }
                    Response.Loading -> {
                        CircularProgressIndicator(color = colorResource(R.color.white), modifier = Modifier
                            .align(Alignment.CenterHorizontally))
                    }
                    is Response.Success -> {
                        HourlyDetails((weatherHourlyForecastStatus.value as Response.Success).data)
                    }
                }

                //Weather Details
                Text(text= stringResource(R.string.daily_details),
                    color = colorResource(R.color.white),
                    fontSize = 32.sp,
                    fontFamily = InterExtraBold,
                    modifier = Modifier.padding(top=20.dp, start = 20.dp))
                WeatherConditions((weatherStatus.value  as Response.Success).data)

                when(weatherDailyForecastStatus.value){
                    is Response.Failure -> {
                        Toast.makeText(context,(weatherDailyForecastStatus.value as Response.Failure).err.message,Toast.LENGTH_SHORT).show()
                    }
                    Response.Loading -> {
                        CircularProgressIndicator(color = colorResource(R.color.white), modifier = Modifier
                            .align(Alignment.CenterHorizontally))
                    }
                    is Response.Success -> {
                        //Daily Forecast
                        Text(text= stringResource(R.string.daily_forecast),
                            color = colorResource(R.color.white),
                            fontSize = 32.sp,
                            fontFamily = InterExtraBold,
                            modifier = Modifier.padding(top=20.dp, start = 20.dp))
                        DailyForecast((weatherDailyForecastStatus.value as Response.Success).data)
                    }
                }
            }
            is Response.Failure -> {
                Toast.makeText(context,(weatherStatus.value as Response.Failure).err.message,Toast.LENGTH_SHORT).show()
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun WeatherDetails(currentWeather: CurrentWeather,address:String,time:String,date:String) {
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
            Text(text=date,
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
            Text(text=time,
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
                .weight(1.5f)
                .padding(top = 30.dp)) {
                Row(modifier = Modifier.padding(top=20.dp)){
                    Text(
                        text = "${currentWeather.main.temp}",
                        color = colorResource(R.color.white),
                        fontSize = 58.sp,
                        fontFamily = InterBold,
                    )
                    Text(
                        text = stringResource(R.string.c),
                        color = colorResource(R.color.white),
                        fontSize = 20.sp,
                        fontFamily = InterMedium,
                    )
                }
                Row(modifier = Modifier.padding(top=10.dp)){
                    Image(
                        painter = painterResource(R.drawable.location_icon2),
                        contentDescription = stringResource(R.string.location_icon),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                    Text(text = address,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white),
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        fontFamily = RobotoRegular
                    )
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp)){
                GlideImage(
                    model = "https://openweathermap.org/img/wn/${currentWeather.weather.get(0).icon}@2x.png",
                    contentDescription = "Icon",
                    modifier = Modifier.size(120.dp)
                )
                Text(text = currentWeather.weather.get(0).description,
                    fontSize = 20.sp,
                    color = colorResource(R.color.white),
                    fontFamily = RobotoRegular,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top=5.dp)
                )
            }
        }
    }

}

@Composable
private fun HourlyDetails(hourlyDetails:List<Deatils>) {
    Box(modifier = Modifier.padding(top=18.dp,start=10.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            items(hourlyDetails.size){
                HourlyDetailsItem(hourlyDetails[it])
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun HourlyDetailsItem(details:Deatils) {
    val splittedDate = details.dt_txt.split(" ")
    val time = splittedDate[1]
    val itemTime = formatTime(time)

    Card (shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.dark_blue)),
        modifier = Modifier
            .width(180.dp)
            .height(150.dp)) {
        Row{
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp)
                .weight(1.2f)
                .align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
                Text(text=itemTime,
                    color = colorResource(R.color.white),
                    fontSize = 20.sp,
                    fontFamily = RobotoRegular
                )
                Row(modifier = Modifier.padding(top=20.dp)){
                    Text(
                        text = "${details.main.temp}",
                        color = colorResource(R.color.white),
                        fontSize = 24.sp,
                        fontFamily = InterBold,
                    )
                    Text(
                        text = stringResource(R.string.c),
                        color = colorResource(R.color.white),
                        fontSize = 12.sp,
                        fontFamily = InterMedium,
                    )
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(start = 10.dp)
                ) {
                GlideImage(
                    model = "https://openweathermap.org/img/wn/${details.weather.get(0).icon}@2x.png",
                    contentDescription = "Icon",
                    modifier = Modifier.size(200.dp)
                )

            }
        }

    }
}

@Composable
private fun WeatherConditions(currentWeather: CurrentWeather) {
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
                    Text(text = "${currentWeather.wind.speed} m/s",
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
                    Text(text = "${currentWeather.clouds.all}%",
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
                    Text(text = "${currentWeather.main.humidity}%",
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
                    Text(text = "${currentWeather.main.pressure} hPa",
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
private fun DailyForecast(dailyDetails:List<Deatils>){
        Card (shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = colorResource(R.color.dark_blue)),
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, bottom = 15.dp)
                .width(370.dp)
                .height(500.dp)){
            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize(unbounded = false)) {
                items(dailyDetails.size){
                    DailyForecastDetails(dailyDetails[it])
                }

            }
        }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DailyForecastDetails(details: Deatils){
    val splittedDate = details.dt_txt.split(" ")
    val date = splittedDate[0]
    val itemDate = formatDate(date)

    Row(modifier = Modifier
        .padding(start = 20.dp)
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = itemDate,
            color = colorResource(R.color.white),
            fontSize = 18.sp,
            fontFamily = InterSemiBold,
            modifier = Modifier.weight(1f)
        )
        GlideImage(
            model = "https://openweathermap.org/img/wn/${details.weather.get(0).icon}@2x.png",
            contentDescription = "Icon",
            modifier = Modifier
                .size(100.dp)
                .weight(1f)
                .padding(end = 15.dp)
        )
        Row(modifier = Modifier.weight(1f)){
            Text(
                text = "${details.main.temp}",
                color = colorResource(R.color.white),
                fontSize = 24.sp,
                fontFamily = InterBold,
            )
            Text(
                text = stringResource(R.string.c),
                color = colorResource(R.color.white),
                fontSize = 12.sp,
                fontFamily = InterMedium,
            )
        }
    }
}