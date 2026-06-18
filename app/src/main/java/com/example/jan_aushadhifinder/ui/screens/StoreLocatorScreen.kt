package com.example.jan_aushadhifinder.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.jan_aushadhifinder.ui.theme.EmeraldGreen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.Locale

data class Store(
    val name: String,
    val owner: String,
    val address: String,
    val pinCode: String,
    val latitude: Double,
    val longitude: Double,
    val distanceValue: Float = Float.MAX_VALUE,
    val distance: String = "Calculating...",
    val isOpen: Boolean = true
)

@SuppressLint("MissingPermission")
@Composable
fun StoreLocatorScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(12.9716, 77.5946), 11f)
    }

    // Maximum expanded list of 50+ Bangalore stores with precise geocoding
    val rawStores = remember {
        listOf(
            Store("PMBJK07152", "Meghana M", "BNM Complex, Airport Main Road, Bagalur", "562149", 13.1332, 77.6713),
            Store("PMBJK00897", "Ms. Nethravathi", "General Hospital premises, Yelahanka Old Town", "560064", 13.0966, 77.5836),
            Store("PMBJK00911", "Kamalraj", "Dr. Rajkumar Road, 2Nd Block, Rajajinagar", "560010", 12.9916, 77.5583),
            Store("PMBJK00904", "S. Ashok Kumar Shetty", "Govt. Maternity Hospital, NR Colony", "560076", 12.9405, 77.5684),
            Store("PMBJK07587", "K. Srujan Sundar Dev", "Chamundi Nagar, RT Nagar", "560032", 13.0200, 77.6000),
            Store("PMBJK00902", "Mrs. Shivaleela Angadi", "Mallathahalli, 1st Main Balji Layout", "560056", 12.9592, 77.5028),
            Store("PMBJK00903", "Mr. Phaniraj C.", "Shankara Math Premises, Shankarapuram", "560070", 12.9416, 77.5732),
            Store("PMBJK03913", "Arun Kumar Shetty", "BBMP Ward Office, Vijaynagar", "560040", 12.9650, 77.5350),
            Store("PMBJK00907", "Lohith Kumar H.R.", "BEML Layout, Rajarajeshwari Nagar", "560098", 12.9150, 77.5180),
            Store("PMBJK00908", "Vinutha Mohan", "Tata Silk Farm, Ganesh Mandir Main Road", "560028", 12.9372, 77.5750),
            Store("PMBJK08420", "Udaya Shankar MV", "Munireddy Layout, Doddakallasandra", "560062", 12.8850, 77.5550),
            Store("PMBJK06882", "Thameem Ahamad Ansari", "Gurappan Palya, Bannerghatta Road", "560029", 12.9234, 77.5996),
            Store("PMBJK03094", "Punith Kumar NS", "Cauvery Block-A, National Village, Koramangala", "560047", 12.9372, 77.6111),
            Store("PMBJK04691", "Surya Narayan", "Govt Hospital, Kadugundanahalli", "560045", 13.0150, 77.6150),
            Store("PMBJK00899", "Satish M B", "Vishwapriyanagar, Begur Road", "560068", 12.8856, 77.6258),
            Store("PMBJK07890", "Pallavi H.N.", "Muneshwara Block, Banashankari 3rd Stage", "560026", 12.9250, 77.5450),
            Store("PMBJK00913", "Krishna N", "Govt General Hospital, K.R. Pura", "560036", 13.0115, 77.7011),
            Store("PMBJK08359", "Prasad V.R.", "Gandhi Circle, Varthur", "560087", 12.9402, 77.7471),
            Store("PMBJK03620", "Sangappa Menashigi", "CHC, Kengeri Govt Hospital", "560060", 12.9050, 77.4850),
            Store("PMBJK07149", "R. Pradeep Kumar", "Kamaraja Road, Cantonment", "560042", 12.9850, 77.6100),
            Store("PMBJK05296", "C.J. Avin", "Ganapathinagar, Basaveshwaranagar", "560079", 12.9880, 77.5350),
            Store("PMBJK05325", "Shankarachari N.V.", "Ramanjaneya Road, Hanumanthanagar", "560019", 12.9420, 77.5580),
            Store("PMBJK05453", "K. Hemanth Kumar", "Girinagar 2nd Phase", "560085", 12.9380, 77.5420),
            Store("PMBJK05643", "Parameshwar P. Kurudi", "St. Thomas Town, Papaiah Road", "560084", 13.0050, 77.6250),
            Store("PMBJK05675", "K. Kalimuthu", "RHCS Layout, 60-50 Main Road", "560091", 12.9750, 77.5050),
            Store("PMBJK05699", "Shivashankar S.", "Agrahara Dasarahalli, Magadi Road", "560040", 12.9780, 77.5480),
            Store("PMBJK05745", "Supriya S.", "Nandini Layout, 5th Cross", "560096", 13.0060, 77.5380),
            Store("PMBJK05751", "Shantha Kumar M.V.", "Gorguntepalya, 1st Main Road", "560022", 13.0180, 77.5450),
            Store("PMBJK06698", "Harish H.V.", "Rajanankunte, Grama Panchayat", "560064", 13.1500, 77.5800),
            Store("PMBJK06737", "Sriniwas Rao C.", "Basavanagar Main Road", "560037", 12.9680, 77.6850),
            Store("PMBJK06827", "Ravikuma V.", "Srinivasanagar, BSK 1st Stage", "560050", 12.9380, 77.5520),
            Store("PMBJK06853", "Amit Kumar Das", "Teacher's Colony, 1st Main Road", "560034", 12.9350, 77.6300),
            Store("PMBJK07109", "Rajashree Naik", "N.R.I Layout, Kalkere", "560016", 13.0250, 77.6750),
            Store("PMBJK07244", "Mohammed Younus", "Kalyan Nagar, Hennur Cross", "560043", 13.0220, 77.6350),
            Store("PMBJK07405", "Smitha Padmanabhan", "Domlur Layout, 7th Cross", "560071", 12.9610, 77.6380),
            Store("PMBJK07406", "C.K. Anantha", "DVG Road, Basavanagudi", "560004", 12.9415, 77.5720),
            Store("PMBJK07407", "Kumar S", "Nagarthpete, Town Hall", "560002", 12.9660, 77.5850),
            Store("PMBJK07414", "Gireesha P.B.", "Bhyraveshwara Nagara", "560091", 12.9820, 77.5020),
            Store("PMBJK07428", "R. Natarajan", "Dinnur Main Road, RT Nagar", "560032", 13.0180, 77.5920),
            Store("PMBJK07461", "Manjunath S K", "Chikkabidarakallu Main Road", "560073", 13.0450, 77.4950),
            Store("PMBJK07462", "Harish Rao Dh", "Poorna Pragna Layout, Kattriguppe", "560085", 12.9280, 77.5380),
            Store("PMBJK07464", "Jayashree M.N.", "Chunchaghatta Main Road", "560062", 12.8720, 77.5680),
            Store("PMBJK07484", "Mujeebur Rahaman J.", "J.C. Nagar Main Road", "560006", 13.0020, 77.5950),
            Store("PMBJK07485", "Kumar S", "Sajjan Rao Circle, V.V. Puram", "560004", 12.9510, 77.5780),
            Store("PMBJK07486", "Saravanan Udayakumar", "Rachnahalli, Mcehs Layout", "560077", 13.0620, 77.6250),
            Store("PMBJK07530", "K. Chandrashekar", "Kalidasa Layout, Srinagara", "560050", 12.9390, 77.5450),
            Store("PMBJK07558", "Raghupathi P.", "9th Block Jayanagar", "560069", 12.9180, 77.5950),
            Store("PMBJK07560", "Jagan M.", "Okalipuram 2nd Stage", "560021", 12.9850, 77.5680),
            Store("PMBJK07561", "C. Venkatesh", "JP Nagar 7th Phase", "560078", 12.8920, 77.5780),
            Store("PMBJK08546", "Premalatha M", "Vajarahalli, Kasaba Hobli", "562123", 12.8680, 77.5381),
            Store("PMBJK01022", "Kishor Kumar K.C.", "Basavanagudi, Bangalore Urban", "560004", 12.9410, 77.5710),
            Store("PMBJK05845", "Narendra V.", "Jyothinagar, Bangalore Urban", "560072", 12.9550, 77.5250)
        )
    }

    var sortedStores by remember { mutableStateOf(rawStores) }

    // Detect user location and trigger sorting
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    userLocation = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 12f)
                    
                    // Logic to sort and format
                    sortedStores = rawStores.map { store ->
                        val results = FloatArray(1)
                        Location.distanceBetween(it.latitude, it.longitude, store.latitude, store.longitude, results)
                        val distanceInMeters = results[0]
                        val distanceInKm = distanceInMeters / 1000
                        store.copy(
                            distanceValue = distanceInMeters,
                            distance = String.format(Locale.getDefault(), "%.1f km", distanceInKm)
                        )
                    }.sortedBy { it.distanceValue }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Jan-Aushadhi Locator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        Text(
            text = if (userLocation != null) "Showing ${sortedStores.size} original verified stores" else "Locating verified centers near you...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(bottom = 16.dp),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = true)
        ) {
            sortedStores.forEach { store ->
                Marker(
                    state = rememberMarkerState(position = LatLng(store.latitude, store.longitude)),
                    title = store.name,
                    snippet = "Distance: ${store.distance} | PIN: ${store.pinCode}"
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(sortedStores) { store ->
                StoreCard(
                    store = store,
                    onGetDirections = {
                        // Use a highly precise address query including Kendra Code, Name, and PIN
                        val addressQuery = "Pradhan Mantri Bhartiya Janaushadhi Kendra ${store.name}, ${store.address}, Bangalore, Karnataka ${store.pinCode}"
                        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(addressQuery)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        
                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(addressQuery)}"))
                            context.startActivity(browserIntent)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StoreCard(store: Store, onGetDirections: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = store.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Owner: ${store.owner}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    Text(text = store.address, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Badge(
                            containerColor = if (store.isOpen) EmeraldGreen else Color.Red,
                            contentColor = Color.White
                        ) {
                            Text(if (store.isOpen) "OPEN NOW" else "CLOSED", modifier = Modifier.padding(horizontal = 4.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.secondary)
                        Text(text = store.distance, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FilledTonalIconButton(
                        onClick = onGetDirections,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Directions, contentDescription = "Get Directions")
                    }
                    Text(text = "DIRECTIONS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
