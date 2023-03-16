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
import com.example.pindrop.classes.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.math.BigInteger
import java.security.MessageDigest

private const val TAG = "JoinGroupFragment"
class JoinGroupFragment: DialogFragment() {

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun getTheme(): Int {
        return R.style.TransparentDialogTheme
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_join_group, container, false)
        // Initialize UI components in your pop-up form here
        val etGroupID = view.findViewById<EditText>(R.id.etGroupID)
        val etGroupPassword = view.findViewById<EditText>(R.id.etGroupPasswordJoin)
        val btJoinGroupSubmit = view.findViewById<Button>(R.id.btJoinGroupSubmit)

        btJoinGroupSubmit.setOnClickListener {
            val groupIDText = etGroupID.text.toString()
            val groupPasswordText = etGroupPassword.text.toString()
            Log.i(TAG, groupIDText)
            Log.i(TAG, groupPasswordText)
            if(groupIDText.isNotEmpty()) {
                updateGroupInFS(groupIDText, groupPasswordText)
            }else {
                etGroupID.error="Cannot be Empty!"
            }
        }

        return view
    }

    private fun updateGroupInFS(GroupID:String, pass:String)
    {
        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        db.collection("Groups").document(GroupID).get().addOnSuccessListener {
                document ->
            if (document.data != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                //val dbPass=db.collection("Groups").whereEqualTo("id", GroupID).toString()
                val GroupDoc=document.toObject<Group>()
                val dbPass= GroupDoc?.password
                if (dbPass != null) {
                    Log.i(TAG, dbPass)
                }
                if (dbPass?.let { checkPassword(pass, it) } == true){
                    user?.uid?.let { db.collection("Users").document(it).update("groups", FieldValue.arrayUnion(GroupID)) }
                    user?.uid?.let { db.collection("Groups").document(GroupID).update("members", FieldValue.arrayUnion(it)) }
                    Log.i(TAG, "Data added")
                    Toast.makeText(activity?.baseContext,"Group Joined Successfully!", Toast.LENGTH_SHORT).show()
                    activity?.recreate()
                    this.dismiss()
                }else {
                    Log.i(TAG, "Wrong Password")
                    Toast.makeText(activity?.baseContext,"Wrong Password Entered", Toast.LENGTH_SHORT).show()
                }
            } else{
                Log.i(TAG, "No such document")
                Toast.makeText(activity?.baseContext,"Group Does Not Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Log.i(TAG, "Error Getting Data")
            Toast.makeText(activity?.baseContext,"Error Retrieving Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPassword(pass:String, dbPass:String):Boolean{
        if(hashPasswordSHA1(pass)==dbPass){
            return true
        }
        return false
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
