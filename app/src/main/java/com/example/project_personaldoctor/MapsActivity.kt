package com.example.project_personaldoctor

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var sensorManager: SensorManager? = null
    private var listener: LocationListener? = null
    var currentLatLng: LatLng? = null
    private var lastUpdate: Long = 0

    var lat = 0.toDouble()
    var lon = 0.toDouble()
    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

        listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                lat = location.latitude
                lon = location.longitude
                currentLatLng = LatLng(location.latitude,location.longitude)
                //println("location:lat,$lat.toString():lon, $lon.toString()")
                //textView!!.append("\n " + loc.longitude + " " + loc.latitude)
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

            }

            override fun onProviderEnabled(s: String) {

            }

            override fun onProviderDisabled(s: String) {

                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }
        }


        requestLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        listener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                lat = location.latitude
//                lon = location.longitude
//                currentLatLng = LatLng(location.latitude,location.longitude)
//                //println("location:lat,$lat.toString():lon, $lon.toString()")
//                //textView!!.append("\n " + loc.longitude + " " + loc.latitude)
//            }

        //locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0L,0F, locationListener)
//        mMap.addMarker(MarketOptions().position(LocationListener).title("test"))
//        lat = 13.7946
//        lon = 100.3234
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(currentLatLng!!.latitude, currentLatLng!!.longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title("Current Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,5f))
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel =
            (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()

        if (accel >= 2) {
            if (actualTime - lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime

            // Initialize a new instance of
            val builder = AlertDialog.Builder(this@MapsActivity)

            // Set the alert dialog title
            builder.setTitle("Emergency Call")

            // Display a message on alert dialog
            builder.setMessage("Are you sure you want to make and emergency call?")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("YES"){dialog, which ->
                val num = "tel:199"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(num))
                startActivity(intent)
                //Toast.makeText(applicationContext,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()
            }


            // Display a negative button on alert dialog
//                builder.setNegativeButton("No"){dialog,which ->
//                    Toast.makeText(applicationContext,"You are not agree.",Toast.LENGTH_SHORT).show()
//                }


            // Display a neutral button on alert dialog
            builder.setNeutralButton("Cancel"){_,_ ->
                Toast.makeText(applicationContext,"You cancelled the emergency call.", Toast.LENGTH_SHORT).show()
            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()

            // Display the alert dialog on app interface
            dialog.show()


            //Toast.makeText(this, "banana", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            10 -> requestLocation()
            else -> {
            }
        }
    }

    internal fun requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET), 10)
            }
            return
        }
        locationManager!!.requestLocationUpdates("gps", 5000, 0f, listener)
        //print("Data In")
//        gpsBtn.setOnClickListener {
//
//           if(currentLatLng!=null) {
//              // location = currentLatLng
//               latText.setText(currentLatLng!!.latitude.toString())
//                lonText.setText(currentLatLng!!.longitude.toString())
//            }
//        }
    }
    //}
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()

        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
    }



}


