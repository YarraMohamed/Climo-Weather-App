package com.example.climo.settings.view

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
import com.example.climo.R
import com.example.climo.view.ui.theme.GradientBackground
import com.example.climo.view.ui.theme.InterBold
import com.example.climo.view.ui.theme.InterExtraBold
import com.example.climo.view.ui.theme.RobotoRegular

@Preview(showSystemUi = true)
@Composable
fun SettingsView() {
    Column(modifier = Modifier
        .fillMaxSize()) {

        //Settings title
        Text(text=stringResource(R.string.settings),
            color = colorResource(R.color.white),
            fontSize = 32.sp,
            fontFamily = InterExtraBold,
            modifier = Modifier.padding(start = 20.dp))

        //Cards
        LanguageCard()
        LocationCard()
        TempCard()
        WindSpeedCard()
    }
}

//LanguageCard
@Composable
private fun LanguageCard(){
    val selectedLanguage = remember { mutableStateOf(R.string.english) }
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
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 27.dp, start = 17.dp)){
                    RadioButtonWithText(
                        text = stringResource(R.string.english),
                        isSelected = selectedLanguage.value == R.string.english,
                        onClick = { selectedLanguage.value = R.string.english }
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.arabic),
                        isSelected = selectedLanguage.value == R.string.arabic,
                        onClick = { selectedLanguage.value = R.string.arabic }
                    )
                }
            }
       }
    }
}

//LocationCard
@Composable
private fun LocationCard(){
    val selectedLocation = remember { mutableStateOf(R.string.gps) }
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
                        isSelected = selectedLocation.value == R.string.gps,
                        onClick = { selectedLocation.value = R.string.gps }
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.map),
                        isSelected = selectedLocation.value == R.string.map,
                        onClick = { selectedLocation.value = R.string.map }
                    )
                }
            }
        }
    }
}

//TempCard
@Composable
private fun TempCard(){
    val selectedTemp = remember { mutableStateOf(R.string.kelvin) }
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
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 27.dp)){
                    RadioButtonWithText(
                        text = stringResource(R.string.kelvin),
                        isSelected = selectedTemp.value == R.string.kelvin,
                        onClick = { selectedTemp.value=R.string.kelvin }
                    )
//                    Spacer(modifier = Modifier.width(2.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.celsius),
                        isSelected = selectedTemp.value == R.string.celsius,
                        onClick = { selectedTemp.value=R.string.celsius }
                    )
//                    Spacer(modifier = Modifier.width(2.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.fahrenheit),
                        isSelected = selectedTemp.value == R.string.fahrenheit,
                        onClick = { selectedTemp.value=R.string.fahrenheit }
                    )
                }
            }
        }
    }
}

//wind speed card
@Composable
private fun WindSpeedCard(){
    val selectedWindSpeed = remember { mutableStateOf(R.string.meter_second) }
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
                        isSelected = selectedWindSpeed.value == R.string.meter_second,
                        onClick = {selectedWindSpeed.value=R.string.meter_second}
                    )
//                    Spacer(modifier = Modifier.width(20.dp))
                    RadioButtonWithText(
                        text = stringResource(R.string.miles_hour),
                        isSelected = selectedWindSpeed.value == R.string.miles_hour,
                        onClick = {selectedWindSpeed.value=R.string.miles_hour}
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

