package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register_Activity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ToLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.CreateAccount.setOnClickListener{
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val username = binding.Username.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && username.isNotEmpty()){
               firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener {
                        val user = firebaseAuth.currentUser
                        user!!.sendEmailVerification().addOnSuccessListener {
                            Toast.makeText(this, "Email verification sent", Toast.LENGTH_SHORT).show()
                        }
                        val db = FirebaseFirestore.getInstance()
                        val usernamemap = hashMapOf(
                            "Username" to username
                        )
                        db.collection("users").document(user.uid).set(usernamemap).addOnSuccessListener {
                            firebaseAuth.signOut()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                   .addOnFailureListener {
                       Toast.makeText(this, it.message ,Toast.LENGTH_LONG)
                           .show()
                   }
            }else{
                Toast.makeText(this, "empty fields are not allowed !!", Toast.LENGTH_LONG).show()
            }
        }
    }
}