package com.example.monkhood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapter
import com.example.model.UserModel
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User


class SecondActivity:AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        userRecyclerView = findViewById(R.id.recyclerView)
        userRecyclerView.layoutManager= LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)

        userArrayList= arrayListOf<UserModel>()
        getUserData()
    }

    private fun getUserData() {

        dbref= FirebaseDatabase.getInstance().getReference("users")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(UserModel::class.java)
                        user?.let { userArrayList.add(it) }
                    }
                    userRecyclerView.adapter = Adapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}