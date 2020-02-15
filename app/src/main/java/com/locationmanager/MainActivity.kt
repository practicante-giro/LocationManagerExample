package com.locationmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private var locationManager : LocationManager? = null

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }


        try {
                // Request location updates
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            } catch(ex: SecurityException) {
                Log.d("myTag", "Security Exception, no location available "+ex)
            }

    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            //thetext.setText("" + location.longitude + ":" + location.latitude)
            val longitudl = location.longitude
            val latitudl=location.latitude

            val late = 20.669731
            val lnge:Double = -103.368986

            val Radius:Int=6371//radius of earth in Km
            val lat1 = late
            val lat2 = latitudl
            val lon1:Double = lnge
            val lon2:Double = longitudl
            val dLat:Double = Math.toRadians(lat2-lat1)
            val dLon:Double = Math.toRadians(lon2-lon1)
            val a:Double = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLon/2) * Math.sin(dLon/2)
            val  c:Double = 2 * Math.asin(Math.sqrt(a))
            val valueResult= Radius*c
            val km:Double=valueResult/1
            val newFormat = DecimalFormat("####")//Kotlin
            //DecimalFormat newFormat = new DecimalFormat("####") //Java
            //val kmInDec =  Integer.valueOf(newFormat.format(km))
            val meter:Double=valueResult*1000
            val meterInDec:Int= Integer.valueOf(newFormat.format(meter))
            //Log.e("Radius Value",""+valueResult+"   KM  "+km+" Meter   "+meter)
            Toast.makeText(this@MainActivity,"Km "+km+"\n"+"metros "+meter,Toast.LENGTH_LONG).show()

            //Toast.makeText(this@MainActivity,"Longitud: "+longitudl,Toast.LENGTH_LONG).show()
            //Toast.makeText(this@MainActivity,"Latitud: "+latitudl,Toast.LENGTH_LONG).show()

            when(meterInDec){
                in meterInDec..100->{

                    metros.setText(meterInDec.toString())
                    rango.setText(true.toString())


                }

                else->{
                    metros.setText(meterInDec.toString())
                    rango.setText(false.toString())

                    Toast.makeText(this@MainActivity,"Estas afuera del rango",Toast.LENGTH_LONG).show()


                }
            }




        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}
