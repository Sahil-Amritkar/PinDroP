package com.example.pindrop.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.classes.Group
import com.example.pindrop.databinding.ActivityAddHeadMarkerBinding
import com.example.pindrop.fragments.GetMarkerDetailsFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

private const val SELECTED_GROUP = "SELECTED_GROUP"
private const val EXTRA_USER_TRIP = "EXTRA_USER_TRIP"
private const val TAG = "AddHeadMarkersActivity"
private const val EXTRA_TRIP_NAME = "EXTRA_TRIP_NAME"
private const val EXTRA_TRIP_DESCRIPTION = "EXTRA_TRIP_DESCRIPTION"
private const val EXTRA_TRIP_STATUS = "EXTRA_TRIP_STATUS"
private const val EXTRA_GROUP = "EXTRA_GROUP"
private const val EXTRA_MARKER_LAT = "EXTRA_MARKER_LAT"
private const val EXTRA_MARKER_LONG= "EXTRA_MARKER_LONG"
private const val EXTRA_MARKER_NAME = "EXTRA_MARKER_NAME"
private const val EXTRA_MARKER_DESC = "EXTRA_MARKER_DESC"
class AddHeadMarkerActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityAddHeadMarkerBinding
    public lateinit var AddedMarker: Marker
    //private lateinit var userTrip: Trip
    private lateinit var btHeadMarkerSubmit: Button
    private lateinit var tripNameText:String
    private lateinit var tripDescriptonText:String
    private lateinit var tripStatus:String
    private lateinit var userGroup: Group

    private lateinit var MarkerNameText:String
    private lateinit var MarkerDescriptonText:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tripNameText = (intent.getSerializableExtra(EXTRA_TRIP_NAME) as? String).toString()
        tripDescriptonText = (intent.getSerializableExtra(EXTRA_TRIP_DESCRIPTION) as? String).toString()
        tripStatus = (intent.getSerializableExtra(EXTRA_TRIP_STATUS) as? String).toString()
        userGroup = (intent.getSerializableExtra(EXTRA_GROUP) as? Group)!!

        supportActionBar?.title = "Add Head Marker for the Trip "

        Toast.makeText(this, "Long Press anywhere to add a marker", Toast.LENGTH_SHORT).show()

        binding = ActivityAddHeadMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(com.example.pindrop.R.id.addHeadMarkerMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


//        btHeadMarkerSubmit=findViewById(R.id.btHeadMarkerSubmit)
//
//        btHeadMarkerSubmit.setOnClickListener{
//            addTripFS()
//
//        }

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
        var markerCount=0
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

        val bundle = Bundle()

        mMap.setOnMapLongClickListener {

            if (markerCount>0){
                AddedMarker.remove()
            }
            markerCount++
            val fragmentManager = supportFragmentManager
            val dialogFragment = GetMarkerDetailsFragment()
            dialogFragment.arguments=bundle
            bundle.putDouble(EXTRA_MARKER_LAT, it.latitude)
            bundle.putDouble(EXTRA_MARKER_LONG, it.longitude)


            dialogFragment.show(fragmentManager, "GetMarkerDetailsFragment")


            AddedMarker= mMap.addMarker(MarkerOptions().position(it))!!
        }





    }

    private fun addTripFS(){

    }
}