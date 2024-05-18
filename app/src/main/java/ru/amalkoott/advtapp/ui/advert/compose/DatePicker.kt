package ru.amalkoott.advtapp.ui.advert.compose

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.ui.zIndex
import java.time.Instant
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFilter(){
    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val minDate = remember { mutableStateOf("") }

    val maxDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val minDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            minDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )
    val maxDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            maxDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, mYear, mMonth, mDay
    )

    Row {
        TextButton(onClick = { minDatePickerDialog.show() }) {
            Column {
                Text(text = "Въезд")
                Text(text = minDate.value)
            }

        }
        TextButton(onClick = { maxDatePickerDialog.show() }) {
            Column {
                Text(text = "Выезд")
                Text(text = maxDate.value)
            }
        }
    }/*
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        // Creating a button that on
        // click displays/shows the DatePickerDialog
        Button(onClick = {
            minDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(Color(0XFF0F9D58)) ) {
            Text(text = "Open Date Picker", color = Color.White)
        }
        // Displaying the mDate value in the Text
        Text(text = "Selected Date: ${minDate.value}", fontSize = 30.sp, textAlign = TextAlign.Center)
    }
    */
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerFilter(openDialog: Boolean,
                          onDismiss: () -> Unit) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = Instant.now().toEpochMilli(),
        initialSelectedEndDateMillis = OffsetDateTime.now().plusDays(8).toInstant().toEpochMilli(),
        yearRange = IntRange(2023, 2100),
        initialDisplayMode = DisplayMode.Picker
    )
    Column(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        DatePickerDialog(
            shape = RoundedCornerShape(6.dp),
            onDismissRequest = onDismiss,
            confirmButton = {
                // Seems broken at the moment with DateRangePicker
                // Works fine with DatePicker
            },
        ) {
            DateRangePicker(
                modifier = Modifier.weight(1f), // Important to display the button
                state = dateRangePickerState,
            )

            TextButton(
                onClick = {
//                    onDismiss()
//                val startDate = dateRangePickerState.selectedStartDateMillis)
//                val endDate = dateRangePickerState.selectedEndDateMillis
                },
                enabled = dateRangePickerState.selectedEndDateMillis != null
            ) {
                Text(text = "Validate")
            }
        }
    }
}
*/
