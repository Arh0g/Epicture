package com.epitech.epicture

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.jar.Manifest

class UploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_upload)

        loadPictureButton.setOnClickListener {
            Toast.makeText(this, "Load Button was clicked !", Toast.LENGTH_SHORT).show()
            loadPicture()
        }
        uploadPictureButton.setOnClickListener {
            Toast.makeText(this, "Upload Button was clicked !", Toast.LENGTH_SHORT).show()
            uploadPicture()
        }

        super.onCreate(savedInstanceState)
    }

    fun loadPicture() {
        if (!checkPermissions())
            allowPermissions()
        Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show()

    }

    fun uploadPicture() {

    }

    fun checkPermissions() : Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true
        return false
    }

    fun allowPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123)
        }
    }

}