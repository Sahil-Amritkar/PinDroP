package com.example.pindrop.activities


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.R
import com.example.pindrop.classes.Trip
import com.example.pindrop.databinding.ActivityDisplayTripMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

private const val SELECTED_GROUP = "SELECTED_GROUP"
private const val EXTRA_USER_TRIP = "EXTRA_USER_TRIP"
private const val TAG = "DisplayMapActivity"
class DisplayTripMapActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayTripMapBinding
    private lateinit var userTrip: Trip
    private lateinit var btEditPins: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userTrip = intent.getSerializableExtra(EXTRA_USER_TRIP) as Trip

        supportActionBar?.title = userTrip.name

        binding = ActivityDisplayTripMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.pindrop.R.id.tripMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        btEditPins=findViewById(R.id.btEditPins)



        btEditPins.setOnClickListener{
            Toast.makeText(this, "Upcoming Feature!", Toast.LENGTH_SHORT).show()
            //val intent = Intent(this, AddTripActivity::class.java)
            //intent.putExtra(EXTRA_USER_MAP, userGroup.name)
            //startActivity(intent)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val success = googleMap.setMapStyle(
            MapStyleOptions(
                resources
                    .getString(com.example.pindrop.R.string.style_json)
            )
        )



        if (!success) {
            Log.e(TAG, "Style parsing failed.")
        }
        mMap = googleMap
        val boundsBuilderAll = LatLngBounds.Builder()
        val boundsBuilderCompleted = LatLngBounds.Builder()


        //Displaying all the head markers of a trip
        for (marker in userTrip.markers!!) {
            var location = marker.longitude?.let { marker.latitude?.let { it1 -> LatLng(it1, it) } }

            location?.let {
                MarkerOptions().position(it).title(marker.name).snippet(marker.description)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(userTrip.markerHue!!)
                    )
            }?.let {
                mMap.addMarker(
                    it
                )
            }

            if (location != null) {
                boundsBuilderAll.include(location)
            }

            //Making the map spawn at the average of all trips
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    boundsBuilderAll.build(),
                    250,
                    250,
                    0
                )
            )

        }

    }
}