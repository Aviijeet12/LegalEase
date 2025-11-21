package com.example.lawclientauth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return

        val ivProfile = findViewById<ImageView>(R.id.ivProfilePic)
        val tvName = findViewById<TextView>(R.id.tvProfileName)
        val tvEmail = findViewById<TextView>(R.id.tvProfileEmail)
        val btnEdit = findViewById<Button>(R.id.btnEditProfile)

        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                tvName.text = doc.getString("name") ?: "User"
                tvEmail.text = doc.getString("email") ?: auth.currentUser?.email
            }

        val imgRef = storage.getReference("profile_images/$uid.jpg")
        imgRef.downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(this).load(uri).into(ivProfile)
            }
            .addOnFailureListener { Glide.with(this).load(R.drawable.ic_profile).into(ivProfile) }

        btnEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
    }
}
