# Google Maps Integration Guide for Jan-Aushadhi Finder

To make the "Store Locator" fully functional with Google Maps, follow these steps:

## Step 1: Get a Google Maps API Key
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. Search for and enable the **Maps SDK for Android**.
4. Go to **APIs & Services > Credentials**.
5. Click **Create Credentials > API Key**. Copy this key.

## Step 2: Configure your project secrets
Create or open the `local.properties` file in your project root and add:
```properties
MAPS_API_KEY=AIzaSy... (your actual key)
```

## Step 3: Update AndroidManifest.xml
The app is already configured to use the `MAPS_API_KEY` from your secrets. Verify that your `AndroidManifest.xml` (usually under `app/src/main`) contains this inside the `<application>` tag:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${MAPS_API_KEY}" />
```

## Step 4: Add Map Permissions
Add these to `AndroidManifest.xml` if not present:
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
```

## Step 5: Replace Placeholder with Real Map & Location Detection
In `StoreLocatorScreen.kt`, you can use this code to enable **Current Location Detection** and show Bangalore stores:

```kotlin
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun BangaloreLiveMap(stores: List<Store>) {
    // Center of Bangalore
    val bangalore = LatLng(12.9716, 77.5946)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bangalore, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(250.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true // <--- THIS DETECTS CURRENT LOCATION
        )
    ) {
        stores.forEach { store ->
            // In a real app, you would have LatLng for each store
            Marker(
                state = rememberMarkerState(position = LatLng(12.9 + Math.random()*0.1, 77.5 + Math.random()*0.1)),
                title = store.name,
                snippet = store.address
            )
        }
    }
}
```

> [!IMPORTANT]
> The project already has `libs.maps.compose` and `libs.play.services.maps` dependencies. You just need the API key to activate the map.
