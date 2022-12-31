package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Register_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private  lateinit var mDbRef: DatabaseReference

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
                        addUserToDB(username, email, firebaseAuth.currentUser?.uid!!)
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


    private fun addUserToDB(username: String, email: String, uid: String){
        mDbRef = FirebaseDatabase.getInstance("https://chat-app-fire-kt-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
        val user = User(username, email, uid)
        mDbRef.child(uid).setValue(user).addOnSuccessListener {
            Toast.makeText(this, "Please Chek your email", Toast.LENGTH_LONG).show()
            Firebase.auth.signOut()
        }
        .addOnFailureListener {
            Toast.makeText(this, "err", Toast.LENGTH_LONG).show()
        }


        //to solve the probleme of adding users to DB
        //Download an updated google-services.json and use that in your app, or
        //Provide the database URL in your code with FirebaseDatabase database = FirebaseDatabase.getInstance("database URL here");

    }
}