package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ToRegister.setOnClickListener{
            val intent = Intent(this, Register_Activity::class.java)
            startActivity(intent)
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()
        binding.signin.setOnClickListener{
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener {
                        if (firebaseAuth.currentUser!!.isEmailVerified) {
                            Toast.makeText(this, "Succesfully signed in", Toast.LENGTH_SHORT).show()
                            val intent2 = Intent(this, ChatApp_Activity::class.java)
                            startActivity(intent2)
                            finish()
                        }else{
                            Toast.makeText(this, "Please check your email", Toast.LENGTH_SHORT).show()
                            firebaseAuth.signOut()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
            }else{
                Toast.makeText(this, "Empty fields are not allowed !!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if (user != null){
            if (user.isEmailVerified) {
                val intent2 = Intent(this, ChatApp_Activity::class.java)
                startActivity(intent2)
                finish()
            }else{
                Toast.makeText(this, "Please check your email", Toast.LENGTH_LONG).show()
            }
        }
    }
}