package com.bradlaronde.smartship

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.bradlaronde.smartship.ui.theme.ComposeTheme
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch

@JsonClass(generateAdapter = true)
internal data class ShipmentsAndDrivers(val shipments: List<String>, val drivers: List<String>)
private data class Shipment(val shipment: String, val driver: String)

private var shipments = mutableStateListOf<Shipment>()

/**
 * Displays a list of drivers that shows best shipment destination when selected.
 */
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                Surface(modifier = Modifier.padding(8.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shipments) { ShipmentCard(it) }
                    }
                }
            }
        }
        lifecycleScope.launch {
            // In practice, the data would probably be read from a server.
            val data = Moshi.Builder().build().adapter(ShipmentsAndDrivers::class.java)
                .fromJson(assets.open("data.json").bufferedReader().use { it.readText() })
            assignDrivers(data)
        }
    }
}

/**
 * Displays a shipment's driver, and the shipment's address when selected.
 */
@Composable
private fun ShipmentCard(shipment: Shipment) {
    var expand by remember { mutableStateOf(false) }
    Card(backgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
        modifier = Modifier.clickable { expand = !expand }) {
        Box(Modifier.padding(8.dp)) {
            Column {
                Text(shipment.driver)
                if (expand) Text(shipment.shipment)
            }
        }
    }
}

/**
 * Assigns the best driver to each shipment.
 */
private fun assignDrivers(shipmentsAndDrivers: ShipmentsAndDrivers?) {
    shipments.clear()
    shipmentsAndDrivers?.shipments?.forEach { street ->
        var greatestSuitability = -1f
        var mostSuitableDriver = ""
        shipmentsAndDrivers.drivers.forEach driver@{ driver ->
            shipments.find { it.driver == driver }?.let { return@driver }
            val vowelCount = driver.vowels().length
            var suitability = if (street.length.even()) vowelCount * 1.5f
            else (driver.length - vowelCount).toFloat()
            val factors = driver.length.factors()
            if (street.length.factors().intersect(factors.toSet()).count() > 1) suitability *= 0.5f
            if (suitability > greatestSuitability) {
                greatestSuitability = suitability
                mostSuitableDriver = driver
            }
        }
        shipments.add(Shipment(street, mostSuitableDriver))
    }
}

/**
 * Returns only the vowels in this string.
 */
private fun String.vowels(): String {
    return this.filter {
        when (it) {
            'a', 'e', 'i', 'o', 'u', 'y' -> true
            else -> false
        }
    }
}

/**
 * Checks if this integer is even.
 */
private fun Int.even(): Boolean {
    return this % 2 == 0
}

/**
 * Returns the factors of this integer.
 */
private fun Int.factors(): List<Int> {
    val factors = mutableListOf<Int>()
    if (this == 0) return factors
    (1..this / 2).filter { this % it == 0 }.forEach { factors.add(it) }
    factors.add(this)
    return factors
}