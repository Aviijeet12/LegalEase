package com.example.lawclientauth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val PICK_IMAGE = 1001
    private var selectedImageUri: Uri? = null

    private lateinit var etName: EditText
    private lateinit var ivProfile: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnPick: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid ?: return

        etName = findViewById(R.id.etEditName)
        ivProfile = findViewById(R.id.ivEditProfilePic)
        btnSave = findViewById(R.id.btnSaveChanges)
        btnPick = findViewById(R.id.btnPickImage)

        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                etName.setText(doc.getString("name") ?: "")
            }

        val ref = storage.getReference("profile_images/$uid.jpg")
        ref.downloadUrl
            .addOnSuccessListener { uri -> Glide.with(this).load(uri).into(ivProfile) }
            .addOnFailureListener { }

        btnPick.setOnClickListener {
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickIntent, PICK_IMAGE)
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("users").document(uid)
                .update("name", name)
                .addOnSuccessListener {
                    if (selectedImageUri != null) {
                        uploadProfileImage(uid)
                    } else {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }
    }

    private fun uploadProfileImage(uid: String) {
        val ref = storage.getReference("profile_images/$uid.jpg")

        val stream = ByteArrayOutputStream()
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val data = stream.toByteArray()

        ref.putBytes(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            ivProfile.setImageURI(selectedImageUri)
        }
    }
}
