package com.example.letschat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letschat.R
import com.example.letschat.adapter.UserAdapter
import com.example.letschat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {

    val userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        usersRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        //Raw data
       /* userList.add(User("Brian","https://pbs.twimg.com/profile_images/966442748990521344/_qX5URB9_400x400.jpg"))
        userList.add(User("John","https://c8.alamy.com/comp/ETTX8J/john-cleese-filming-the-british-comedy-film-monty-python-and-the-holy-ETTX8J.jpg"))
        userList.add(User("Eric","https://upload.wikimedia.org/wikipedia/en/thumb/c/cb/Flyingcircus_2.jpg/250px-Flyingcircus_2.jpg"))
        userList.add(User("Terry","https://static01.nyt.com/images/2020/01/23/obituaries/23jones/merlin_60759704_20e5c09f-c54b-4040-9c9e-9144117552a1-mobileMasterAt3x.jpg"))
        userList.add(User("Graham","https://advancelocal-adapter-image-uploads.s3.amazonaws.com/image.oregonlive.com/home/olive-media/width2048/img/tv/photo/2018/10/10/montycastjpg-7ef393e2355a42aa.jpg"))
        */
        getUserList()

    }

    fun getUserList(){
        var firebase: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        var databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for(dataSpanShot:DataSnapshot in snapshot.children) {
                    val user = dataSpanShot.getValue(User::class.java)
                    if (user?.userId?.equals(firebase?.uid) == true) {
                        user?.let { userList.add(it) }
                    }
                }
                val userAdapter = UserAdapter(applicationContext,userList)

                usersRecyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}