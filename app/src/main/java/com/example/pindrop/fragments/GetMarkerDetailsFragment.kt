package com.example.pindrop.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pindrop.R
import com.example.pindrop.activities.MainActivity
import com.example.pindrop.classes.Group
import com.example.pindrop.classes.Marker
import com.example.pindrop.classes.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


private const val TAG = "GetMarkerDetailFragment"
private const val EXTRA_MARKER_NAME = "EXTRA_MARKER_NAME"
private const val EXTRA_MARKER_DESC = "EXTRA_MARKER_DESC"

private const val EXTRA_TRIP_NAME = "EXTRA_TRIP_NAME"
private const val EXTRA_TRIP_DESCRIPTION = "EXTRA_TRIP_DESCRIPTION"
private const val EXTRA_TRIP_STATUS = "EXTRA_TRIP_STATUS"
private const val EXTRA_GROUP = "EXTRA_GROUP"
private const val EXTRA_MARKER_LAT = "EXTRA_MARKER_LAT"
private const val EXTRA_MARKER_LONG= "EXTRA_MARKER_LONG"

class GetMarkerDetailsFragment : DialogFragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore

    private lateinit var tripNameText:String
    private lateinit var tripDescriptonText:String
    private lateinit var tripStatus:String
    private lateinit var userGroup: Group

    private lateinit var markerNameText:String
    private lateinit var markerDescText:String


    override fun getTheme(): Int {
        return R.style.TransparentDialogTheme
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_get_marker_details, container, false)

        val etMarkerName = view.findViewById<EditText>(R.id.etMarkerName)
        val etMarkerDescription = view.findViewById<EditText>(R.id.etMarkerDescription)
        val btMarkerDetailsSubmit = view.findViewById<Button>(R.id.btMarkerDetailsSubmit)



        btMarkerDetailsSubmit.setOnClickListener {
            markerNameText = etMarkerName.text.toString()
            markerDescText = etMarkerDescription.text.toString()

            if(markerNameText.isNotEmpty()) {
                addTripFS()
            }else {
                etMarkerName.error="Cannot be Empty!"
            }
        }

        return view
    }

    private fun addTripFS(){
        tripNameText = (activity?.intent?.getSerializableExtra(EXTRA_TRIP_NAME) as? String).toString()
        tripDescriptonText = (activity?.intent?.getSerializableExtra(EXTRA_TRIP_DESCRIPTION) as? String).toString()
        tripStatus = (activity?.intent?.getSerializableExtra(EXTRA_TRIP_STATUS) as? String).toString()
        userGroup = (activity?.intent?.getSerializableExtra(EXTRA_GROUP) as? Group)!!

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        val groupID=userGroup.id!!

        val bundle = this.arguments

        val headMarkerToAdd=Marker(
            name = markerNameText,
            description = markerDescText,
            latitude = bundle?.getDouble(EXTRA_MARKER_LAT),
            longitude = bundle?.getDouble(EXTRA_MARKER_LONG)
        )

        val tripToAdd=Trip(
            name= tripNameText,
            description = tripDescriptonText,
            status = tripStatus,
            headMarker = headMarkerToAdd,
            markerHue = createMarkerHue(),
            markers = mutableListOf(headMarkerToAdd))

        db.collection("Groups").document(groupID).update("trips", FieldValue.arrayUnion(tripToAdd))

        Toast.makeText(context, "Trip Added Successfully!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(activity, MainActivity::class.java))

    }

    private fun createMarkerHue():Float{
        var markerHue:Float
        if(tripStatus=="Completed") {
            markerHue = rand(100, 360)
        } else{
            markerHue= 0f
        }
        return markerHue
    }

    private fun rand(start: Int, end: Int): Float {
        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
        return (Random(System.nanoTime()).nextInt(end - start + 1) + start).toFloat()
    }

}