package com.example.pindrop.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pindrop.R
import com.example.pindrop.classes.Group
import com.example.pindrop.classes.GroupsAdapter
import com.example.pindrop.classes.User
import com.example.pindrop.classes.UserGroup
import com.example.pindrop.fragments.CreateGroupFragment
import com.example.pindrop.fragments.JoinGroupFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


private const val TAG = "MainActivity"
const val EXTRA_USER_MAP="EXTRA_USER_MAP"
class MainActivity : AppCompatActivity() {
    private lateinit var rvGroups: RecyclerView
    private lateinit var btCreateGroup: Button
    private lateinit var btJoinGroup: Button
    private lateinit var userGroups: MutableList<UserGroup>
    private lateinit var groupAdapter: GroupsAdapter
    private lateinit var tvHelloUser: TextView
    private lateinit var btLogout: Button

    private lateinit var mAuth: FirebaseAuth
    private val db = Firebase.firestore



    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        rvGroups = findViewById(R.id.rvGroups)
        btCreateGroup = findViewById(R.id.btCreateGroup)
        btJoinGroup = findViewById(R.id.btJoinGroup)
        //tvHelloUser = findViewById((R.id.tvHelloUser))
        btLogout = findViewById(R.id.btLogout)

        //tvHelloUser.text="Hello "+user?.displayName+"!"
        supportActionBar?.title = "Hi " + user?.displayName + "!" + " Your Groups"

        //userGroups = generateSampleData()

        // set layout manager on the recycler view
        rvGroups.layoutManager = LinearLayoutManager(this)

        //Log.i(TAG, "User Groups: " + userGroups)


        //userGroups = deserializeUserGroups(this).toMutableList()

        displayUserGroups()


        // set adapter on the recycler view
//        groupAdapter = GroupsAdapter(this, userGroupsFS, object : GroupsAdapter.OnClickListener {
//            override fun onItemClick(position: Int) {
//                Log.i(TAG, "onItemClick $position")
//                val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
//                intent.putExtra(EXTRA_USER_MAP, userGroups[position])
//                startActivity(intent)
//            }
//        })
//        rvGroups.adapter = groupAdapter


        //If Create Group is clicked
        btCreateGroup.setOnClickListener {
            Log.i(TAG, "Tap on Create Group")
            val fragmentManager = supportFragmentManager
            val dialogFragment = CreateGroupFragment()
            dialogFragment.show(fragmentManager, "CreateGroupFragment")
        }

        //If Join Group is clicked
        btJoinGroup.setOnClickListener {
            Log.i(TAG, "Tap on Join Group")
            val fragmentManager = supportFragmentManager
            val dialogFragment = JoinGroupFragment()
            dialogFragment.show(fragmentManager, "JoinGroupFragment")
        }

        //Logout
        btLogout.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
//
//        fabCreateGroup.setOnClickListener{
//            Log.i(TAG, "Tap on FAB")
//            showAlertDialog()
//        }
    }

    private fun displayUserGroups() {

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

//        var userDoc: User
//        if (user != null) {
//            db.collection("Users").document(user.uid).get().addOnSuccessListener { document ->
//                if (document.data != null) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        userDoc = document.toObject<User>()!!
//                        Log.i(TAG, "User Obj: " + userDoc)
//                        userGroupsFS = getGroupDetails(userDoc)
//                        Log.i(TAG, "User Groups FS: " + userGroupsFS)
//
//                        groupAdapter = GroupsAdapter(
//                            this,
//                            userGroupsFS,
//                            object : GroupsAdapter.OnClickListener {
//                                override fun onItemClick(position: Int) {
//                                    Log.i(TAG, "onItemClick $position")
//                                    val intent =
//                                        Intent(this@MainActivity, DisplayMapActivity::class.java)
//                                    intent.putExtra(EXTRA_USER_MAP, userGroups[position])
//                                    startActivity(intent)
//                                }
//                            })
//                        rvGroups.adapter = groupAdapter
//
//                    }, 500)
//
//                } else {
//                    Log.i(TAG, "No such document")
//                    Toast.makeText(this, "Group Does Not Exist", Toast.LENGTH_SHORT).show()
//                }
//            }.addOnFailureListener {
//                Log.i(TAG, "Error Getting Data")
//                Toast.makeText(this, "Error Retrieving Data", Toast.LENGTH_SHORT).show()
//            }
//        }

        var userGroupsFS = mutableListOf<Group>()

        var userDoc: User
        if (user != null) {
            db.collection("Users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.data != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        userDoc = document.toObject<User>()!!

                        //Get Groups from the Group IDs
                        for(group in userDoc.groups!!) {
                            db.collection("Groups").document(group).get().
                            addOnSuccessListener { document ->
                                if (document.data != null) {
                                    Log.i(TAG, "DocumentSnapshot data: ${document.data}")
                                    val groupDoc= document.toObject<Group>()!!
                                    userGroupsFS.add(groupDoc)
                                    // Log.i(TAG, userDoc.name!!)
                                }
                            }
                        }

                        //Display the stuff and Call Maps activity if clicked
                        Handler(Looper.getMainLooper()).postDelayed({
                            groupAdapter = GroupsAdapter(
                                this,
                                userGroupsFS,
                                object : GroupsAdapter.OnClickListener {
                                    override fun onItemClick(position: Int) {
                                        Log.i(TAG, "onItemClick $position")
                                        val intent = Intent(this@MainActivity, DisplayMapActivity::class.java)
                                        intent.putExtra(EXTRA_USER_MAP, userGroupsFS[position])
                                        startActivity(intent)
                                    }
                                })
                            rvGroups.adapter = groupAdapter
                        }, 500)


                    } else {
                        Log.i(TAG, "No such document")
                        Toast.makeText(this, "Group Does Not Exist", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Log.i(TAG, "Error Getting Data")
                    Toast.makeText(this, "Error Retrieving Data", Toast.LENGTH_SHORT).show()

                }
        }


    }

}