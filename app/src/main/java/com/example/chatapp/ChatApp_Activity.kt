package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ActivityChatAppBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ChatApp_Activity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdaper
    private lateinit var binding: ActivityChatAppBinding
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDbRef = FirebaseDatabase.getInstance("https://chat-app-fire-kt-default-rtdb.europe-west1.firebasedatabase.app/").getReference()

        userList = ArrayList()
        adapter = UserAdaper(this, userList)
        userRecyclerView = binding.userRecyclerView
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (Firebase.auth.currentUser!!.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Firebase.auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onContextItemSelected(item)
    }
}