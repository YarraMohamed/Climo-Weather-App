package com.example.climo.alerts.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.climo.R
import com.example.climo.alerts.viewmodel.AlertsViewModel
import com.example.climo.model.Alerts
import com.example.climo.model.Response
import com.example.climo.utilities.ApplicationUtils
import com.example.climo.utilities.ErrorAnimation
import com.example.climo.view.ui.theme.InterMedium
import com.example.climo.view.ui.theme.InterSemiBold
import com.example.climo.view.ui.theme.RobotoMedium
import com.example.climo.view.ui.theme.RobotoRegular
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AlertView(viewModel: AlertsViewModel,address:String) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var alertsList = viewModel.alerts.collectAsStateWithLifecycle()
    var messageState = viewModel.message.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val openDialog = remember { mutableStateOf(false) }
    var fromTime by remember { mutableStateOf("Select Time") }
    var toTime by remember { mutableStateOf("Select Time") }
    var date by remember { mutableStateOf("Select date") }
    var errorMessage by remember { mutableStateOf("") }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if (!isGranted) {
                val rationale = shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                if (rationale) {
                    android.app.AlertDialog.Builder(context)
                        .setTitle("Permission Required")
                        .setMessage("Notification permission is necessary. Please enable it in settings.")
                        .setPositiveButton("Go to Settings") { _, _ ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            context.startActivity(intent)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }

        }

    )


    LaunchedEffect(messageState.value) {
        if(messageState.value.isNotBlank())
            snackBarHostState.showSnackbar(messageState.value, duration = SnackbarDuration.Short)
    }

    when(alertsList.value){
        is Response.Failure -> {
        ErrorAnimation()
        Toast.makeText(context,"Error in getting data", Toast.LENGTH_SHORT).show()
    }
        Response.Loading -> {
            Row(horizontalArrangement = Arrangement.Center){
                CircularProgressIndicator(color = colorResource(R.color.white))
            }
        }
        is Response.Success -> {
            Scaffold(
                snackbarHost = { SnackbarHost(snackBarHostState) },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            openDialog.value = true
                        },
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
                    AlertsList(viewModel,(alertsList.value as Response.Success).data)
                }
            }
            if (openDialog.value) {
                InputAlertDialog(
                    viewModel = viewModel,
                    address= address,
                    date = date,
                    fromTime = fromTime,
                    toTime = toTime,
                    errorMessage = errorMessage,
                    onDismissRequest = { openDialog.value = false },
                    onTimeSelected = { d, from, to, error ->
                        date = d
                        fromTime = from
                        toTime = to
                        errorMessage = error
                    }
                )
            }
        }
    }
}

@Composable
private fun AlertsList(viewModel: AlertsViewModel,alerts: List<Alerts>){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp), contentAlignment = Alignment.Center) {
        if(alerts.isEmpty()){
            AlertsAnimation()
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp),contentPadding = PaddingValues(bottom = 80.dp)){
            items(alerts.size) {
                AlertItem(alerts[it]){
                    viewModel.deleteAlert(alerts[it])
                }
            }
        }
    }
}

@Composable
private fun AlertItem(alerts: Alerts,action:()->Unit){
    Log.i("Worker", "AlertItem: $alerts ")
    val context = LocalContext.current
    Card(
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.dark_blue)
        ),
        modifier = Modifier
            .width(380.dp)
            .height(80.dp)){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()){
            Text(text="date\n${alerts.date}",
                fontSize = 18.sp,
                color = colorResource(R.color.white),
                fontFamily = RobotoMedium,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(R.drawable.arrow_icon),
                contentDescription = stringResource(R.string.arrow_icon),
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 13.dp, end = 20.dp)
            )
            Text(text="Time\n${alerts.startTime}",
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
                    .padding(end = 35.dp)
                    .align(Alignment.CenterVertically)
            )

            Image(
                painter = painterResource(R.drawable.bin_icon),
                contentDescription = stringResource(R.string.bin_icon),
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 10.dp)
                    .clickable(onClick = {
                        android.app.AlertDialog.Builder(context)
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
    viewModel: AlertsViewModel,
    address: String,
    date:String,
    fromTime: String,
    toTime: String,
    errorMessage: String,
    onDismissRequest: () -> Unit,
    onTimeSelected: (String, String, String,String) -> Unit
) {
    var tempFromTime by remember { mutableStateOf(fromTime) }
    var tempToTime by remember { mutableStateOf(toTime) }
    var tempDate by remember { mutableStateOf(date) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
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
                showDatePicker = true
            },colors = ButtonDefaults.buttonColors(colorResource(R.color.white))) {
                Text(text="date: $tempDate", color = colorResource(R.color.blue), fontSize = 14.sp, fontFamily = RobotoRegular, modifier = Modifier.width(200.dp))
            }
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
                    onTimeSelected(tempDate,tempFromTime, tempToTime, errorText)
                    if (tempFromTime == "Select Time" || tempToTime == "Select Time") {
                        errorText = "Please select valid times."
                    } else {
                        val newAlert = Alerts(date=tempDate,startTime = tempFromTime, endTime = tempToTime, address = address.split(" ")[0])
                        viewModel.addAlert(newAlert)
                        onDismissRequest()
                    }
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
    if(showDatePicker){
        AlertDatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { day, month, year ->
                val formattedDate = formatDate(day, month, year)
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.MONTH, month)
                    set(Calendar.YEAR,year)
                }
                val currentCalendar = Calendar.getInstance()

                if (selectedCalendar.before(currentCalendar)) {
                    errorText = "Selected date is in the past. Choose a valid time."
                    showDatePicker = false
                    return@AlertDatePickerDialog
                }

                tempDate = formattedDate
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Int, Int, Int) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    Dialog(onDismissRequest = onDismissRequest) {
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pick a Date", fontSize = 18.sp)
                DatePicker(state = datePickerState)
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance().apply { timeInMillis = millis }
                            onDateSelected(
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.YEAR)
                            )
                        }
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

fun formatDate(day: Int, month: Int, year: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return SimpleDateFormat("MMMM, d", Locale.getDefault()).format(calendar.time)
}

