package com.ener.meregister

import android.R.attr
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.ener.meregister.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import android.R.attr.password
import com.google.firebase.database.FirebaseDatabase


private lateinit var binding: ActivityRegisterBinding

class Register : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()


        RegisterNewUser()

    }


    private fun RegisterNewUser() {

        binding.createAccountButton.setOnClickListener {

            // Perfoming User Input Checks
            if (binding.fullName.text.toString().trim().isEmpty()) {
                binding.fullName.setError("Full Name is required!")
                binding.fullName.requestFocus()
                return@setOnClickListener
            }

            if (binding.age.text.toString().trim().isEmpty()) {
                binding.age.setError("Age is required!")
                binding.age.requestFocus()
                return@setOnClickListener
            }
            if (binding.emailReg.text.toString().trim().isEmpty()) {
                binding.emailReg.setError("Input a valid email!")
                binding.emailReg.requestFocus()
                return@setOnClickListener
            }
            if (binding.passwordReg.text.toString().isEmpty()) {
                binding.passwordReg.setError("Input a valid password!")
                binding.passwordReg.requestFocus()
                return@setOnClickListener
            }
            // Ending Checks



            binding.progressBarReg.isVisible = true
            binding.fullName.isEnabled = false
            binding.age.isEnabled = false
            binding.passwordReg.isEnabled = false
            binding.emailReg.isEnabled = false




            mAuth?.createUserWithEmailAndPassword(
                binding.emailReg.text.toString(),
                binding.passwordReg.text.toString()
            )
                ?.addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val userdata = User(
                            binding.fullName.text.toString().trim(),
                            binding.age.text.toString().trim(),
                            binding.emailReg.text.toString().trim(),
                            binding.passwordReg.text.toString()
                        )
                        Log.d(TAG, "createUserWithEmail:success")
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userdata)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Account Creation Successful.Verify email!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                    binding.progressBarReg.isVisible = false
                                }else{
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        this, "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBarReg.isVisible = false
                                }
                            }
                        // Go BAck to Login Page for user to Login!!!
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progressBarReg.isVisible = false
                        //Done!
                    }

                }


        }

    }



}
