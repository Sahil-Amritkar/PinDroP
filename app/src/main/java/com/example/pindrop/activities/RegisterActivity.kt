package com.example.pindrop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private const val TAG = "RegisterActivity"
class RegisterActivity : AppCompatActivity() {
    private lateinit var etEnterEmailRegister: EditText
    private lateinit var etEnterNameRegister: EditText
    private lateinit var etEnterPasswordRegister: EditText
    private lateinit var btRegister: Button
    private lateinit var tvLoginInstead: TextView

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        etEnterEmailRegister= findViewById(R.id.etEnterEmailRegister)
        etEnterNameRegister= findViewById(R.id.etEnterNameRegister)
        etEnterPasswordRegister= findViewById(R.id.etEnterPasswordRegister)
        btRegister= findViewById(R.id.btRegister)
        tvLoginInstead= findViewById(R.id.tvLoginInstead)

        mAuth = FirebaseAuth.getInstance()

        btRegister.setOnClickListener{
            createUser()
        }

        tvLoginInstead.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

    }

    private fun createUser()
    {
        val email=etEnterEmailRegister.text.toString().trim()
        val name=etEnterNameRegister.text.toString().trim()
        val password=etEnterPasswordRegister.text.toString().trim()

        if(email.isNotEmpty() and Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (password.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        mAuth.signInWithEmailAndPassword(email, password)
                        val req=userProfileChangeRequest {
                            displayName = name
                        }
                        var user = mAuth.currentUser
                        user?.updateProfile(req)
                        //Add user to FS after 2s so that name data can get uploaded
                        Handler(Looper.getMainLooper()).postDelayed({
                            addUserToFS()
                        }, 2000)
                        Toast.makeText(this@RegisterActivity, "Registered Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@RegisterActivity, "Error in Registration :(", Toast.LENGTH_SHORT).show()
                    }
            } else {
                etEnterPasswordRegister.error = "Cannot be Empty!"
            }
        }else{
            etEnterEmailRegister.error="Enter Valid Email! "
        }

    }

    private fun addUserToFS(){
        var user=mAuth.currentUser
        val newuser = hashMapOf(
            "id" to user?.uid,
            "name" to user?.displayName.toString(),
            "email" to user?.email.toString(),
            "groups" to emptyList<String>()
        )

        if (user != null) {
            db.collection("Users").document(user.uid).set(newuser)
                .addOnSuccessListener {
                    Log.i(TAG, "DocumentSnapshot added")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
}

