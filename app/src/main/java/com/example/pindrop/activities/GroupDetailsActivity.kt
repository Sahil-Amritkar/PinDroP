package com.example.pindrop.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pindrop.R
import com.example.pindrop.classes.Group
import com.example.pindrop.classes.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

private const val TAG = "GroupDetailsActivity"
private const val SELECTED_GROUP = "SELECTED_GROUP"
class GroupDetailsActivity : AppCompatActivity() {

    private lateinit var tvGroupNameText:TextView
    private lateinit var tvGroupIDText:TextView
    private lateinit var tvGroupMembersText:TextView
    private lateinit var btEditGroupDetails:Button

    private lateinit var userGroup: Group

    private val db = Firebase.firestore

    private lateinit var userDoc:User

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        userGroup = intent.getSerializableExtra(SELECTED_GROUP) as Group

        displayGroupDetails(userGroup)

//        //hardcoded for now
//        val GroupID="GROUP-18e42ba1"
//
//        db.collection("Groups").document(GroupID).get().addOnSuccessListener {
//                document ->
//            if (document.data != null) {
//                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                val GroupDoc=document.toObject<Group>()
//                if (GroupDoc != null) {
//                    displayGroupDetails(GroupDoc)
//                }
//            }
//            else{
//                Log.i(TAG, "No such document")
//                Toast.makeText(this,"Group Does Not Exist", Toast.LENGTH_SHORT).show()
//            }
//        }.addOnFailureListener {
//            Log.i(TAG, "Error Getting Data")
//            Toast.makeText(this,"Error Retrieving Data", Toast.LENGTH_SHORT).show()
//        }

        //supportActionBar?.title = "Hi "+user?.displayName+"!"+" Your Groups"

    }

    private fun displayGroupDetails(groupInfo:Group)
    {
        tvGroupNameText = findViewById(R.id.tvGroupNameText)
        tvGroupIDText = findViewById(R.id.tvGroupIDText)
        tvGroupMembersText = findViewById(R.id.tvGroupMembersText)
        btEditGroupDetails = findViewById(R.id.btEditGroupDetails)



        var memberNames = mutableListOf<String>()
        for(member in groupInfo.members!!) {

            if (member != null) {
                db.collection("Users").document(member).get().addOnSuccessListener { document ->
                    if (document.data != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        userDoc= document.toObject<User>()!!
                        memberNames.add(userDoc.name.toString())
                        // Log.i(TAG, userDoc.name!!)
                    }
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            Log.i(TAG, "Member names list: "+memberNames.toString())
            var membersText=""
            for((count, name) in memberNames.withIndex()){
                val srno=count+1
                if(count!=0) {
                    Log.i(TAG, "F1"+membersText)
                    membersText = membersText+"\n"+srno.toString()+". "+name
                }
                else{
                    Log.i(TAG, "F2"+membersText)
                    membersText = membersText+srno.toString()+". "+name
                }
            }
            tvGroupNameText.text=groupInfo.name
            tvGroupIDText.text=groupInfo.id
            tvGroupMembersText.text=membersText
        }, 500)

//        Log.i(TAG, "F3"+userDoc.name)


        btEditGroupDetails.setOnClickListener {
            Toast.makeText(this, "Upcoming Feature", Toast.LENGTH_SHORT).show()
        }
    }
}





