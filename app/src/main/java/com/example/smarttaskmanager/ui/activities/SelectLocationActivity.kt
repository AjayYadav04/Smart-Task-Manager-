package com.example.smarttaskmanager.ui.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smarttaskmanager.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class SelectLocationActivity : BaseActivity(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentFrameLayout = findViewById<FrameLayout>(R.id.content_frame)
        contentFrameLayout.addView(layoutInflater.inflate(R.layout.activity_select_location, null))
        setCustomHeader("Location", tittleVisible = true, backButtonVisible = true)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fetchLastLocation()
        }

        findViewById<Button>(R.id.confirm_location).setOnClickListener {
            currentLocation?.let {
                val locationName = getLocationName(it.latitude, it.longitude)
                val locationArea = getLocationArea(it.latitude, it.longitude)
                setResult(RESULT_OK, Intent().apply {
                    putExtra("location_name", locationName)
                    putExtra("location_area", locationArea)
                })
                finish()
            } ?: Toast.makeText(this, "Please wait while we fetch your location.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLocation = it
                    (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)
                        ?.getMapAsync(this)
                } ?: Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        currentLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            map?.addMarker(MarkerOptions().position(latLng).title("Current Location"))
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLastLocation()
        } else {
            showLocationPermissionDeniedDialog()
        }
    }

    private fun showLocationPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Permission Required")
            .setMessage("Please grant location permission to use this feature.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun getLocationName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.featureName ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    private fun getLocationArea(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.subAdminArea ?: ""
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
