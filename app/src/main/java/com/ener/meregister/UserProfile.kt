package com.ener.meregister

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.ener.meregister.databinding.ActivityUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

private lateinit var binding:ActivityUserProfileBinding

class UserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nameId.isFocusable = false
        binding.ageId.isFocusable = false
        binding.passwordId.isFocusable = false
        binding.emailId.isFocusable = false


        CheckIfUserIsLoggedIn()

        GetUserData()

        SignOutUser()
    }

    private fun GetUserData() {
        val user = Firebase.auth.currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users")


        // Get Data from users directory matching with current logged in user
        if (user != null) {
            ref.child(user).get().addOnSuccessListener {
                if (it.exists()){
                    //Add Info here
                    val names = it.child("fullname").value.toString()
                    val ages = it.child("age").value.toString()
                    val emails = it.child("email").value.toString()
                    val passwords = it.child("password").value.toString()

                    binding.nameId.setText(names)
                    binding.emailId.setText(emails)
                    binding.ageId.setText(ages)
                    binding.passwordId.setText(passwords)




                }else{
                    Toast.makeText(this,"Sorry, your account does not exist!",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }
        }





    }

    private fun SignOutUser() {
        binding.signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun CheckIfUserIsLoggedIn() {
        val auth = FirebaseAuth.getInstance().currentUser
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null || !auth!!.isEmailVerified) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}