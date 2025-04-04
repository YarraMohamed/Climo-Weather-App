package com.example.climo.settings.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.climo.R
import com.example.climo.settings.viewmodel.SettingsViewModel
import com.example.climo.view.NavigationRoutes
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.InterBold
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.RobotoRegular

@Composable
fun SettingsView(viewModel: SettingsViewModel,navController:NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()) {

        //Settings title
        Text(text=stringResource(R.string.settings),
            color = colorResource(R.color.white),
            fontSize = 32.sp,
            fontFamily = InterExtraBold,
            modifier = Modifier.padding(start = 20.dp))

        //Cards
        LanguageCard(viewModel)
        LocationCard(viewModel,navController)
        TempCard(viewModel)
        WindSpeedCard(viewModel)
    }
}

//LanguageCard
@Composable
private fun LanguageCard(viewModel: SettingsViewModel){
    val language by viewModel.language.collectAsStateWithLifecycle()
    val selectedLanguage = remember { mutableStateOf(language) }

    //Container
    Column(modifier = Modifier
        .padding(top = 25.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        //The Whole Card
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.dark_blue)),
            modifier = Modifier
                .width(360.dp)
                .height(125.dp)) {
            //Content
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                //Row for the title
                Row(modifier = Modifier.padding(top=20.dp)){
                    Image(
                        painter = painterResource(R.drawable.language_icon),
                        contentDescription = stringResource(R.string.language_icon),
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text= stringResource(R.string.language),
                        fontSize = 24.sp,
                        color = colorResource(R.color.white),
                        fontFamily = InterBold,
                    )
                }
                //Row for Radio Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 27.dp, start = 17.dp)
                ) {
                    RadioButtonWithText(
                        text = stringResource(R.string.english),
                        isSelected = selectedLanguage.value == "en",
                        onClick = {
                            selectedLanguage.value = "en"
                            viewModel.saveLanguage("en")
                        }
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.arabic),
                        isSelected = selectedLanguage.value == "ar",
                        onClick = {
                            selectedLanguage.value = "ar"
                            viewModel.saveLanguage("ar")
                        }
                    )
                }
            }
       }
    }
}

//LocationCard
@Composable
private fun LocationCard(viewModel: SettingsViewModel,navController: NavHostController){
    val location by viewModel.savedLocationOption.collectAsStateWithLifecycle()
    val selectedOptions = remember { mutableStateOf(location) }

    //Container
    Column(modifier = Modifier
        .padding(top = 25.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        //Card
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.dark_blue)),
            modifier = Modifier
                .width(360.dp)
                .height(125.dp)) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                //Row for the title
                Row(modifier = Modifier.padding(top=14.dp)){
                    Image(
                        painter = painterResource(R.drawable.location_icon),
                        contentDescription = stringResource(R.string.location_icon),
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.location),
                        fontSize = 24.sp,
                        color = colorResource(R.color.white),
                        fontFamily = InterBold,
                    )
                }
                //Row for the radio button
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 17.dp, top = 27.dp)) {
                    RadioButtonWithText(
                        text = stringResource(R.string.gps),
                        isSelected = selectedOptions.value == "GPS",
                        onClick = {
                            selectedOptions.value = "GPS"
                            viewModel.saveLocationOption("GPS")
                        }
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.map),
                        isSelected = selectedOptions.value == "MAP",
                        onClick = {
                            selectedOptions.value = "MAP"
                            viewModel.saveLocationOption("MAP")
                            navController.navigate(NavigationRoutes.Map)
                        }
                    )
                }
            }
        }
    }
}

//TempCard
@Composable
private fun TempCard(viewModel: SettingsViewModel){
    val temp by viewModel.tempUnit.collectAsStateWithLifecycle()
    val selectedTemp = remember { mutableStateOf(temp) }
    Log.i("Settings", "TempCard: ${selectedTemp.value} ")
    //container
    Column(modifier = Modifier
        .padding(top = 25.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        //card
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.dark_blue)),
            modifier = Modifier
                .width(360.dp)
                .height(125.dp)) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                //row for title
                Row(modifier = Modifier.padding(top=14.dp)){
                    Image(
                        painter = painterResource(R.drawable.temp_icon),
                        contentDescription = stringResource(R.string.temp_icon),
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.temp_unit),
                        fontSize = 24.sp,
                        color = colorResource(R.color.white),
                        fontFamily = InterBold,
                    )
                }
                //row for radio buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 27.dp)
                ) {
                    RadioButtonWithText(
                        text = stringResource(R.string.kelvin),
                        isSelected = selectedTemp.value == "",
                        onClick = {
                            selectedTemp.value = ""
                            viewModel.saveTempUnit("")
                            viewModel.saveWindSpeedUnit()
                        }
                    )
                    RadioButtonWithText(
                        text = stringResource(R.string.celsius),
                        isSelected = selectedTemp.value == "metric",
                        onClick = {
                            selectedTemp.value = "metric"
                            viewModel.saveTempUnit("metric")
                            viewModel.saveWindSpeedUnit()
                        }
                    )
                    RadioButtonWithText(
                        text = stringResource(R.string.fahrenheit),
                        isSelected = selectedTemp.value == "imperial",
                        onClick = {
                            selectedTemp.value = "imperial"
                            viewModel.saveTempUnit("imperial")
                            viewModel.saveWindSpeedUnit()
                        }
                    )
                }
            }
        }
    }
}

//wind speed card
@Composable
private fun WindSpeedCard(viewModel: SettingsViewModel){

    val windSpeedUnit by viewModel.windSpeed.collectAsStateWithLifecycle()
    val tempUnit by viewModel.tempUnit.collectAsStateWithLifecycle()

    LaunchedEffect(tempUnit) {
        viewModel.getWindSpeedUnit()
    }

    //container
    Column(modifier = Modifier
        .padding(top = 15.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        //card
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.dark_blue)),
            modifier = Modifier
                .width(360.dp)
                .height(125.dp)) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                //row for title
                Row(modifier = Modifier.padding(top=14.dp)){
                    Image(
                        painter = painterResource(R.drawable.wind_speed_icon),
                        contentDescription = stringResource(R.string.wind_speed_icon),
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.wind_speed),
                        fontSize = 24.sp,
                        color = colorResource(R.color.white),
                        fontFamily = InterBold,
                    )
                }
                //row for radio button
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 17.dp, top = 27.dp)) {
                    RadioButtonWithText(
                        text = stringResource(R.string.meter_second),
                        isSelected = windSpeedUnit == "",
                        onClick = { viewModel.getWindSpeedUnit() }
                    )
//                    Spacer(modifier = Modifier.width(20.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.miles_hour),
                        isSelected = windSpeedUnit == "m/h",
                        onClick = {viewModel.getWindSpeedUnit()}
                    )
                }
            }
        }
    }
}

@Composable
private fun RadioButtonWithText(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.White,
                unselectedColor = Color.White
            ))
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = text, color = Color.White, fontSize = 18.sp, fontFamily = RobotoRegular)
    }
}

