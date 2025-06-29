package com.palettex.codingroundpractice.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.palettex.codingroundpractice.data.entity.ForecastWeatherResponse
import com.palettex.codingroundpractice.data.entity.WeatherResponse
import com.palettex.codingroundpractice.presentation.viewmodel.WeatherViewModel
import com.palettex.codingroundpractice.ui.theme.CodingRoundPracticeTheme

@Composable
fun MainPage(viewModel: WeatherViewModel = viewModel()) {
    val currentWeatherState by viewModel.currentWeatherState.collectAsState()
    val forecastWeatherState by viewModel.forecastWeatherState.collectAsState()

    LaunchedEffect(Unit) {
         viewModel.getCurrentWeather("Taiwan")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            WeatherActionButtons(
                onFetchConcurrently = {
                    viewModel.fetchWeatherDataConcurrently("Taiwan", 3)
                },
                onFetchSequentially = {
                    viewModel.fetchWeatherDataSequentially("Taiwan", 3)
                },
                onCancel = {
                    viewModel.cancelAllRequests()
                }
            )

            CurrentWeatherSection(currentWeatherState)

            Spacer(modifier = Modifier.height(24.dp))

            ForecastWeatherSection(forecastWeatherState)
        }
    }
}

@Composable
fun WeatherActionButtons(
    onFetchConcurrently: () -> Unit,
    onFetchSequentially: () -> Unit,
    onCancel: () -> Unit
) {
    Log.d("GDT", "WeatherActionButtons recomposed")

    // Concurrent fetch button - faster execution
    Button(
        onClick = onFetchConcurrently,
    ) {
        Column {
            Text(
                text = "Fetch Weather Concurrently",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "⚡ Faster (~2 seconds)",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Sequential fetch button - slower but shows current weather first
    Button(
        onClick = onFetchSequentially,
    ) {
        Column {
            Text(
                text = "Fetch Weather Sequentially",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "🔄 Shows current first (~4 seconds)",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Cancel button
    OutlinedButton(
        onClick = onCancel
    ) {
        Text("Cancel All Requests")
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun CurrentWeatherSection(currentWeatherState: UiState<WeatherResponse>) {
    Text(
        text = "Current Weather",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(8.dp))

    when (currentWeatherState) {
        is UiState.Idle -> {
            Text("Ready to load current weather...")
        }
        is UiState.Loading -> {
            Text(
                text = "Loading current weather...",
                color = Color.Blue
            )
        }
        is UiState.Success -> {
            val weather = currentWeatherState.data

            // Testing api no response specific value,
            // moshi set it to default value
            // Log.d("GDT","test_value=" + weather.testValue.equals("123"))
            // Log.d("GDT","test_value=" + weather.testValue)

            Column {
                Text(
                    text = "Location: ${weather.location.name}",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Temperature: ${weather.current.tempC}°C",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Last Updated: ${weather.current.lastUpdated}",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
        is UiState.Error -> {
            Text(
                text = "Error loading current weather: ${currentWeatherState.message}",
                color = Color.Red
            )
        }
    }
}

@Composable
fun ForecastWeatherSection(forecastWeatherState: UiState<ForecastWeatherResponse>) {
    Text(
        text = "Forecast Weather",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(8.dp))

    when (forecastWeatherState) {
        is UiState.Idle -> {
            Text("Click button to load forecast weather...")
        }
        is UiState.Loading -> {
            Text(
                text = "Loading forecast weather...",
                color = Color.Blue
            )
        }
        is UiState.Success -> {
            val forecast = forecastWeatherState.data
            Column {
                forecast.forecast.forecastday.forEach { day ->
                    Text(
                        text = "Date: ${day.date}",
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Avg Temperature: ${day.day.avgTempC}°C",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
        is UiState.Error -> {
            Text(
                text = "Error loading forecast weather: ${forecastWeatherState.message}",
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    CodingRoundPracticeTheme {
        MainPage()
    }
}