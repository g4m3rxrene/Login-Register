package com.ener.meregister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.view.isVisible
import com.ener.meregister.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

private lateinit var binding: ActivityMainBinding
private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        GoToRegisterScreen()
        LoginTheUser()

        GoToFrogotPasswordScreen()

    }

    private fun GoToFrogotPasswordScreen() {
        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun LoginTheUser() {
        auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            val email = binding.emailLogin.text.toString().trim()
            val password = binding.passwordLogin.text.toString()
            if (email.isEmpty()) {
                binding.emailLogin.setError("Email is required!")
                binding.emailLogin.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLogin.setError("Please enter a valid email!")
                binding.emailLogin.requestFocus()
                return@setOnClickListener

            }

            if (password.isEmpty()) {
                binding.emailLogin.setError("Please enter a valid password!")
                binding.passwordLogin.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.passwordLogin.setError("Minimum password length is 6 characters!")
                binding.passwordLogin.requestFocus()
                return@setOnClickListener

            }

            binding.progressBar.isVisible = true

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        val auth = FirebaseAuth.getInstance().currentUser
                        if (auth!!.isEmailVerified) {
                            val intent = Intent(this, UserProfile::class.java)
                            //This Clears all activity stack....whatever that is(remember to google),also don't forget to put it at every intent call
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            //Redirect to User Account
                        } else {
                            auth.sendEmailVerification()
                            Toast.makeText(
                                this,
                                "Verify Your Account",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBar.isVisible = false


                        }


                    } else {
                        Toast.makeText(this, "Incorrect email/password", Toast.LENGTH_SHORT).show()
                        binding.progressBar.isVisible = false
                    }
                }


        }


    }

    private fun GoToRegisterScreen() {

        binding.createAccount.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }


}


data class User(val fullname: String, val age: String, val email: String, val password: String)