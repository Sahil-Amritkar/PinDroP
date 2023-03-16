package com.example.pindrop.activities

//import com.example.pindrop.activities.databinding.ActivityDisplayMapBinding
//import com.example.pindrop.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.R
import com.example.pindrop.classes.Group
import com.example.pindrop.databinding.ActivityDisplayGroupMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

private const val SELECTED_GROUP = "SELECTED_GROUP"
private const val EXTRA_USER_TRIP = "EXTRA_USER_TRIP"
private const val TAG = "DisplayGroupMapActivity"
private const val EXTRA_GROUP = "EXTRA_GROUP"
class DisplayMapActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayGroupMapBinding
    private lateinit var userGroup: Group
    private lateinit var btGroupInfo: Button
    private lateinit var btAddTrip: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userGroup = intent.getSerializableExtra(EXTRA_USER_MAP) as Group

        supportActionBar?.title = userGroup.name

        binding = ActivityDisplayGroupMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.pindrop.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btGroupInfo=findViewById(R.id.btGroupInfo)
        btAddTrip=findViewById(R.id.btAddTrip)

        btGroupInfo.setOnClickListener{
            val intent = Intent(this, GroupDetailsActivity::class.java)
            intent.putExtra(SELECTED_GROUP, userGroup)
            startActivity(intent)
        }

        btAddTrip.setOnClickListener{
            val intent = Intent(this, AddTripActivity::class.java)
            intent.putExtra(EXTRA_GROUP, userGroup)
            startActivity(intent)
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
        for (trip in userGroup.trips!!) {
            var location = trip.headMarker?.let { it.latitude?.let { it1 -> trip.headMarker!!.longitude?.let { it2 ->
                LatLng(it1,
                    it2
                )
            } } }
            if (trip.status == "Completed") {
                location?.let {
                    MarkerOptions().position(it).title(trip.name).snippet(trip.description)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(trip.markerHue!!)
                        )
                }?.let {
                    mMap.addMarker(
                        it
                    )
                }
                if (location != null) {
                    boundsBuilderCompleted.include(location)
                }
            } else {
                location?.let {
                    MarkerOptions().position(it).title(trip.name).snippet(trip.description)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(trip.markerHue!!)
                        )
                }?.let {
                    mMap.addMarker(
                        it
                    )
                }
            }
            if (location != null) {
                boundsBuilderAll.include(location)
            }

            //Making the map spawn at the average of all trips
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    boundsBuilderAll.build(),
                    500,
                    500,
                    0
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({
                //Making the map spawn at the average of completed trips
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        boundsBuilderCompleted.build(),
                        500,
                        500,
                        0
                    )
                )
            }, 3000)



            mMap.setOnInfoWindowLongClickListener {
                //delete the trip
            }

            mMap.setOnInfoWindowClickListener {mapHeadMarker ->
                val intent = Intent(this, DisplayTripMapActivity::class.java)
                for(trip in userGroup.trips!!){
                    if (mapHeadMarker.title==trip.name && mapHeadMarker.snippet==trip.description){
                        intent.putExtra(EXTRA_USER_TRIP, trip)
                        startActivity(intent)

                    }
                }


            }
        }

    }

}