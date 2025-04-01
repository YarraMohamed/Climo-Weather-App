package com.example.climo.alerts.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.climo.R
import com.example.climo.model.Alerts
import com.example.climo.view.ui.theme.InterMedium
import com.example.climo.view.ui.theme.InterSemiBold
import com.example.climo.view.ui.theme.RobotoMedium
import com.example.climo.view.ui.theme.RobotoRegular
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AlertView() {
    val openDialog = remember { mutableStateOf(false) }
    var fromTime by remember { mutableStateOf("Select Time") }
    var toTime by remember { mutableStateOf("Select Time") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openDialog.value = true },
                containerColor = colorResource(R.color.white),
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.notification_icon),
                    contentDescription = stringResource(R.string.notification_icon),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(R.string.alerts),
                color = colorResource(R.color.white),
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
            AlertsList(emptyList())
        }
    }
    if (openDialog.value) {
        InputAlertDialog(
            fromTime = fromTime,
            toTime = toTime,
            errorMessage = errorMessage,
            onDismissRequest = { openDialog.value = false },
            onTimeSelected = { from, to, error ->
                fromTime = from
                toTime = to
                errorMessage = error
            }
        )
    }
}

@Composable
private fun AlertsList(alerts: List<Alerts>){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center) {
        if(alerts.isEmpty()){
            AlertsAnimation()
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
                painter = painterResource(R.drawable.bin_icon),
                contentDescription = stringResource(R.string.bin_icon),
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 10.dp)

            )
        }
    }
}

@Composable
private fun AlertsAnimation(){

    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.alerts_animation)
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

@Composable
private fun InputAlertDialog(
    fromTime: String,
    toTime: String,
    errorMessage: String,
    onDismissRequest: () -> Unit,
    onTimeSelected: (String, String, String) -> Unit
) {
    var tempFromTime by remember { mutableStateOf(fromTime) }
    var tempToTime by remember { mutableStateOf(toTime) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isSelectingFromTime by remember { mutableStateOf(true) }
    var errorText by remember { mutableStateOf(errorMessage) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(colorResource(R.color.blue), RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.set_your_alert),
                color = colorResource(R.color.white),
                fontSize = 36.sp,
                fontFamily = InterSemiBold
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                isSelectingFromTime = true
                showTimePicker = true
            },colors = ButtonDefaults.buttonColors(colorResource(R.color.white))) {
                Text(text="From: $tempFromTime", color = colorResource(R.color.blue), fontSize = 14.sp, fontFamily = RobotoRegular, modifier = Modifier.width(200.dp))
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                isSelectingFromTime = false
                showTimePicker = true
            },colors = ButtonDefaults.buttonColors(colorResource(R.color.white))) {
                Text(text = "To: $tempToTime", color = colorResource(R.color.blue), fontSize = 14.sp, fontFamily = RobotoRegular, modifier = Modifier.width(200.dp))
            }
            if (errorText.isNotEmpty()) {
                Text(errorText, color = colorResource(R.color.white), fontSize = 18.sp, fontFamily = InterSemiBold, modifier = Modifier.padding(start=20.dp,top=25.dp))
            }
            Spacer(modifier = Modifier.height(35.dp))
            Row {
                Button(onClick = { onDismissRequest() },colors = ButtonDefaults.buttonColors(colorResource(R.color.white))) {
                    Text(stringResource(R.string.cancel),color = colorResource(R.color.blue), fontSize = 18.sp, fontFamily = RobotoRegular)
                }
                Spacer(modifier = Modifier.width(25.dp))
                Button(onClick = {
                    onTimeSelected(tempFromTime, tempToTime, errorText)
                    onDismissRequest()
                },colors = ButtonDefaults.buttonColors(colorResource(R.color.white))) {
                    Text(stringResource(R.string.save), color = colorResource(R.color.blue), fontSize = 18.sp, fontFamily = RobotoRegular)
                }
            }
        }

    }

    if (showTimePicker) {
        AlertTimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                val formattedTime = formatTime(hour, minute)
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                val currentCalendar = Calendar.getInstance()

                if (selectedCalendar.before(currentCalendar)) {
                    errorText = "Selected time is in the past. Choose a valid time."
                    showTimePicker = false
                    return@AlertTimePickerDialog
                }

                if (!isSelectingFromTime) {
                    val fromCalendar = Calendar.getInstance().apply {
                        val parts = tempFromTime.split(":", " ")
                        if (parts.size == 3) {
                            set(Calendar.HOUR, parts[0].toInt())
                            set(Calendar.MINUTE, parts[1].toInt())
                        }
                    }
                    if (selectedCalendar.before(fromCalendar)) {
                        errorText = "End time cannot be earlier than start time."
                        showTimePicker = false
                        return@AlertTimePickerDialog
                    }
                }

                if (isSelectingFromTime) {
                    tempFromTime = formattedTime
                } else {
                    tempToTime = formattedTime
                }
                showTimePicker = false
                errorText = ""
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertTimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    val timePickerState = rememberTimePickerState()
    Dialog(onDismissRequest = onDismissRequest) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pick a Time", fontSize = 18.sp)
                TimePicker(state = timePickerState)
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = {
                        onTimeSelected(timePickerState.hour, timePickerState.minute)
                        onDismissRequest()
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

fun formatTime(hour: Int, minute: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
}

