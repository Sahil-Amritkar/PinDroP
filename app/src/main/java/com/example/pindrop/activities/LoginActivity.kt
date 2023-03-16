package com.example.pindrop.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var etEnterEmailLogin: EditText
    private lateinit var etEnterPasswordLogin: EditText
    private lateinit var btLogin: Button
    private lateinit var tvRegisterInstead: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        etEnterEmailLogin= findViewById(R.id.etEnterEmailLogin)
        etEnterPasswordLogin= findViewById(R.id.etEnterPasswordLogin)
        btLogin= findViewById(R.id.btLogin)
        tvRegisterInstead= findViewById(R.id.tvRegisterInstead)

        mAuth = FirebaseAuth.getInstance()

        etEnterEmailLogin.setText("demoemail@gmail.com")
        etEnterPasswordLogin.setText("password")

        btLogin.setOnClickListener{
            loginUser()
        }

        tvRegisterInstead.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


    }

    private fun loginUser(){


        val email=etEnterEmailLogin.text.toString().trim()
        val password=etEnterPasswordLogin.text.toString().trim()




        if(email.isNotEmpty() and Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (password.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(this@LoginActivity, "Logged In Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@LoginActivity, "Error in Logging In :(", Toast.LENGTH_SHORT).show()
                    }
            } else {
                etEnterPasswordLogin.error = "Cannot be Empty!"
            }
        }else{
            etEnterEmailLogin.error="Enter Valid Email! "
        }

    }
}