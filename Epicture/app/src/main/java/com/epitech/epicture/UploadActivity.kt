package com.epitech.epicture

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.IOException
import java.util.jar.Manifest

class UploadActivity : AppCompatActivity() {

    var pathContent: String = ""

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

    /*This member function allow to catch when user load picture*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*Good, we catch signal*/
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            var imageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val imageView = findViewById(R.id.imageViewLoad) as ImageView
            imageView.setImageBitmap(bitmap)
            /*Get the path of the picture*/
            pathContent = getPathContent(imageUri)
        }
    }

    fun getPathContent(uri: Uri) : String {
        var id = uri.lastPathSegment.split(":")[1]
        val imageContent = arrayOf(MediaStore.Images.Media.DATA)
        val stateEnvironment = Environment.getExternalStorageState()
        var photoNew: Uri
        if (stateEnvironment.equals(Environment.MEDIA_MOUNTED, ignoreCase = true))
            photoNew = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        else
            photoNew = MediaStore.Images.Media.INTERNAL_CONTENT_URI
        var cursor = contentResolver.query(
            photoNew,
            imageContent,
            MediaStore.Images.Media._ID + "=" + id,
            null,
            null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        cursor.close()
        return ""
    }

    fun loadPicture() {
        if (!checkPermissions())
            allowPermissions()
        Toast.makeText(this, "Permission Granted !", Toast.LENGTH_SHORT).show()
        //
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Load your picture"), 1)
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