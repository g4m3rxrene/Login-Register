package com.ener.meregister

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.view.isVisible
import com.ener.meregister.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding: ActivityForgotPasswordBinding
class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        SendEmailToResetPassword()




    }

    private fun SendEmailToResetPassword() {
        binding.resetPasswordButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val email = binding.resetPasswordEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.resetPasswordEmail.setError("Email is required!")
                binding.resetPasswordEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.resetPasswordEmail.setError("Please enter a valid email!")
                binding.resetPasswordEmail.requestFocus()
                return@setOnClickListener

            }
            binding.resetPasswordProgressbar.isVisible = true

            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this,"Check your mail inbox to continue",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Oops! Something wrong happened, try again...",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Log.d(TAG, "${it.message}")
            }

        }

    }
}