package com.example.pindrop.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.classes.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private const val TAG = "AddTripActivity"
private const val REQUEST_CODE=1234
private const val EXTRA_TRIP_NAME = "EXTRA_TRIP_NAME"
private const val EXTRA_TRIP_DESCRIPTION = "EXTRA_TRIP_DESCRIPTION"
private const val EXTRA_TRIP_STATUS = "EXTRA_TRIP_STATUS"
private const val EXTRA_GROUP = "EXTRA_GROUP"
class AddTripActivity : AppCompatActivity() {

    private lateinit var etEnterTripName:EditText
    private lateinit var etEnterTripDescription:EditText
    private lateinit var swCompleted: Switch
    private lateinit var btContinueAddHeadMarker: Button

    private lateinit var userGroup: Group

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.pindrop.R.layout.activity_add_trip)

        supportActionBar?.title = "Add New Memories!"

        etEnterTripName = findViewById(com.example.pindrop.R.id.etEnterTripName)
        etEnterTripDescription = findViewById(com.example.pindrop.R.id.etEnterTripDescription)
        swCompleted = findViewById(com.example.pindrop.R.id.swCompleted)
        btContinueAddHeadMarker = findViewById(com.example.pindrop.R.id.btContinueAddHeadMarker)

        userGroup = intent.getSerializableExtra(EXTRA_GROUP) as Group

        mAuth= FirebaseAuth.getInstance()
        val user =mAuth.currentUser


        var tripStatus: String? =null

        swCompleted?.setOnCheckedChangeListener { _, isChecked ->
            tripStatus = if (isChecked) "Completed" else "Future"
        }

        //If Continue is clicked go to Add Head Marker Activity
        btContinueAddHeadMarker.setOnClickListener{
            val tripNameText=etEnterTripName.text.toString()
            val tripDescriptionText=etEnterTripDescription.text.toString()
            Log.i(TAG,"HI")
            val intent = Intent(this, AddHeadMarkerActivity::class.java)
            intent.putExtra(EXTRA_TRIP_NAME, tripNameText)
            intent.putExtra(EXTRA_TRIP_DESCRIPTION, tripDescriptionText)
            intent.putExtra(EXTRA_TRIP_STATUS, tripStatus)
            intent.putExtra(EXTRA_GROUP, userGroup)
            startActivity(intent)
        }

    }

}
