package com.example.lawclientauth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawclientauth.databinding.ActivityUploadDocumentBinding

class UploadDocumentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadDocumentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseFile.setOnClickListener {
            Toast.makeText(this, "File picker will be added in backend phase.", Toast.LENGTH_SHORT).show()
        }

        binding.btnUploadNow.setOnClickListener {
            Toast.makeText(this, "Uploaded (mock). Backend integration later.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
