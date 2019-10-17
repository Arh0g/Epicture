package com.epitech.epicture

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.*
import okhttp3.Request
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.jar.Manifest

class UploadActivity : AppCompatActivity() {

    var pathContent: String = ""
    var uriPathContent: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_upload)

        loadPictureButton.setOnClickListener {
            Toast.makeText(this, "Load Button was clicked !", Toast.LENGTH_SHORT).show()
            loadPicture()
        }
        uploadPictureButton.setOnClickListener {
            Toast.makeText(this, "Upload Button was clicked !", Toast.LENGTH_SHORT).show()
            if (!uploadPicture())
                return@setOnClickListener
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

    fun prepareImageFromPath(uri: Uri) : Boolean {
        /*First Step*/
        val streamByte = ByteArrayOutputStream()
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, streamByte)
        val realBitmap = Base64.encodeToString(File(pathContent).readBytes(), 0)

        /*Second Step*/
        val textImage = findViewById<EditText>(R.id.editTextTitle).text.toString()
        val textDescription = findViewById<EditText>(R.id.editTextDescription).text.toString()

        if (textImage == "") {
            Toast.makeText(this, "Your post needs a title.", Toast.LENGTH_SHORT).show()
            return false
        } else if (textDescription == "") {
            Toast.makeText(this, "Your post needs a description.", Toast.LENGTH_SHORT).show()
            return false
        }

        /*Third Step*/
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("type", "file")
            .addFormDataPart("image", realBitmap)
            .addFormDataPart("name", textImage)
            .addFormDataPart("title", textImage)
            .addFormDataPart("description", textDescription)
            .build()

        /*Create POST Request from steps*/
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.imgur.com/3/upload/")
            .post(requestBody)
            .addHeader("Authorization", "Bearer " + imgurClient.accessToken)
            .addHeader("Cache-control", "no-cache")
            .build()
        FormBody.Builder().build()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                finish()
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute the request.")
            }
        })
        return true
    }

    fun getPathContent(uri: Uri) : String {
        uriPathContent = uri

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

    fun uploadPicture() : Boolean {
        if (!prepareImageFromPath(uriPathContent!!))
            return false
        return true
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