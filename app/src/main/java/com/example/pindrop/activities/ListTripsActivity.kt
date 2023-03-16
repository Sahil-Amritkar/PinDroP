package com.example.pindrop.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.R

class ListTripsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_trips)

        supportActionBar?.title = "Your Trips"


    }
}