package com.example.pindrop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.pindrop.R
import com.example.pindrop.R.layout
import com.example.pindrop.R.style
import com.example.pindrop.classes.Trip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


private const val TAG = "CreateGroupFragment"
class CreateGroupFragment : DialogFragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun getTheme(): Int {
        return style.TransparentDialogTheme
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(layout.fragment_create_group, container, false)
        // Initialize UI components in your pop-up form here
        val etGroupName = view.findViewById<EditText>(R.id.etGroupName)
        val etGroupPassword = view.findViewById<EditText>(R.id.etGroupPassword)
        val btCreateGroupSubmit = view.findViewById<Button>(R.id.btCreateGroupSubmit)

        btCreateGroupSubmit.setOnClickListener {
            val groupNameText = etGroupName.text.toString()
            val groupPasswordText = etGroupPassword.text.toString()
            Log.i(TAG, groupNameText)
            Log.i(TAG, groupPasswordText)
            if(groupNameText.isNotEmpty()) {
                addGroupToFS(groupNameText, groupPasswordText)
            }else {
                etGroupName.error="Cannot be Empty!"
            }
        }

        return view
    }

    private fun addGroupToFS(name:String, pass:String)
    {
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val groupID = "GROUP-" + UUID.randomUUID().toString().substring(0,8)
//        val group=Group(
//            groupID,
//            name,
//            hashPasswordSHA1(pass),
//            mutableListOf(user?.uid),
//            emptyList<Trip>() as MutableList<Trip>
//        )
        var group = hashMapOf(
            "id" to groupID,
            "name" to name,
            "password" to hashPasswordSHA1(pass),
            "members" to mutableListOf(user?.uid),
            "trips" to emptyList<Trip>()
        )

        if (user != null) {
            db.collection("Groups").document(groupID).set(group)
                .addOnSuccessListener {
                    Log.i(TAG, "DocumentSnapshot added")
                    Toast.makeText(context, "Group Created Successfully!", Toast.LENGTH_SHORT).show()
                    activity?.recreate()
                    this.dismiss()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(context, "Error in Creating Group :(", Toast.LENGTH_SHORT).show()
                }
        }

        user?.uid?.let { db.collection("Users").document(it).update("groups", FieldValue.arrayUnion(groupID)) }


    }

    private fun hashPasswordSHA1(pass:String): String {
        val md = MessageDigest.getInstance("SHA-1")
        val messageDigest = md.digest(pass.toByteArray())
        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        return hashtext
    }
}





