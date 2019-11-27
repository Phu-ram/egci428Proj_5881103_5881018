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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map_test.*

class MapTestActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0
    private var currentLatLng: LatLng? = null
    lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_test)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                LocationBox.text= location!!.latitude.toString() +" "+location!!.longitude.toString()

                currentLatLng = LatLng(location!!.latitude, location!!.longitude)

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }
        }
        requestLocation()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            10 -> requestLocation()
            else -> {}
        }
    }
    private fun requestLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET), 10)
            }
            return
        }
        locationManager!!.requestLocationUpdates("gps", 5000, 0f, locationListener)
        gpsBtn.setOnClickListener {
            println("in gps butt")
            if(currentLatLng!=null) {
                println("in gps butt"+currentLatLng!!.latitude)
                latText.setText(currentLatLng!!.latitude.toString())
                lonText.setText(currentLatLng!!.longitude.toString())
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        //val mu = LatLng(13.793406,100.322514)

        //mMap.addMarker(MarkerOptions().position(mu).title("Marker in MU"))
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mu, 8f))

        mapBtn.setOnClickListener {
            //var latLonTxt: LatLng
            //latLonTxt = LatLng(latText.text.toString().toDouble(), lonText.text.toString().toDouble())
            mMap.addMarker(MarkerOptions().position(currentLatLng!!).title(currentLatLng.toString()))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 8f))
        }
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
            val builder = AlertDialog.Builder(this@MapTestActivity)

            // Set the alert dialog title
            builder.setTitle("Emergency Call")

            // Display a message on alert dialog
            builder.setMessage("Are you sure you want to make an emergency call?")

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

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()

        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
    }


}
