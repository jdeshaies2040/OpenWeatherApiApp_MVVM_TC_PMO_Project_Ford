package com.example.openweatherapiapp_mvvm_tc_pmo_project_ford

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.model.WeatherResponse
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.states.WeatherResult
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.ui.theme.OpenWeatherApiApp_MVVM_TC_PMO_Project_FordTheme
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            OpenWeatherApiApp_MVVM_TC_PMO_Project_FordTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherPage(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = weatherViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherPage(modifier: Modifier = Modifier, viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult by viewModel.cityWeather.observeAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Search City") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.fetchWeather(city) }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

        when (val result = weatherResult) {
            is WeatherResult.Success -> WeatherDetails(data = result.data)
            is WeatherResult.Error -> Text(text = result.message ?: "Unknown Error", color = Color.Red)
            WeatherResult.Loading -> CircularProgressIndicator()
            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = data.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${(data.main.temp - 273.15).toInt()} °C",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = data.weather.getOrNull(0)?.description ?: "",
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeatherKeyVal("Humidity", "${data.main.humidity}%")
            WeatherKeyVal("Wind Speed", "${data.wind.speed} m/s")
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall)
        Text(text = key, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenWeatherApiApp_MVVM_TC_PMO_Project_FordTheme {
        Text("Weather App Preview")
    }
}
