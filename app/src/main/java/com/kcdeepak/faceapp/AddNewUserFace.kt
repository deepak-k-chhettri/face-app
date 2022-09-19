package com.kcdeepak.faceapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.DexterBuilder.MultiPermissionListener
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException



class AddNewUserFace : AppCompatActivity() {
    val CAMERA_REQUEST_CODE = 1
    val GALLERY_REQUEST_CODE = 2
    lateinit var myViewModel:MyViewModel

    lateinit var imageToBeLoaded: ImageView
    lateinit var saveButton:Button
    var imageUri: Uri? = null
    lateinit var bitmap: Bitmap
    lateinit var editTextName:EditText
    lateinit var editTextPhone: EditText
    lateinit var editTextAddress: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user_face)

        myViewModel = MyViewModel(this)
        imageToBeLoaded = findViewById(R.id.imageToBeAdded)
        saveButton = findViewById(R.id.saveButton)
        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextAddress = findViewById(R.id.editTextAddress)

        imageToBeLoaded.setOnClickListener {
//            val galleryIntent = Intent()
//            galleryIntent.setType("image/*")
//                .action = Intent.ACTION_GET_CONTENT
//
//            startActivityForResult(Intent.createChooser(galleryIntent,"Pick Image"),PICK_IMAGE)

            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select an Option")
            val pictureDialogItem = arrayOf("Open Gallery","Open Camera")
            pictureDialog.setItems(pictureDialogItem){
                dialog, which ->
                when(which){
                    0->checkGalleryPermission()
                    1->checkCameraPermission()
                }
            }
            pictureDialog.show()

            Toast.makeText(this,"CLICKED",Toast.LENGTH_LONG).show()
        }

        saveButton.setOnClickListener {
            val userFace =
                UserFace(0,
                    editTextName.text.toString(),
                    editTextPhone.text.toString(),
                    editTextAddress.text.toString(),
                    bitmap)

            myViewModel.insertFaceUser(userFace)
            
            intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkCameraPermission() {
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.CAMERA,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(
                object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                camera()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?,
                    ) {
                        showRotationDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    private fun showRotationDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("It seems like you haven't given permission for this feature.")
            .setPositiveButton("Go to Settings"){
                _,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }
                catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    private fun camera() {
        intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMERA_REQUEST_CODE)
    }
    private fun gallery() {
        intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,GALLERY_REQUEST_CODE)

    }

    private fun checkGalleryPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(
                object : PermissionListener{
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        gallery()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddNewUserFace,"Permission denied",Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?,
                    ) {
                        showRotationDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode== RESULT_OK){
//            data!!.data.also { imageUri = it }
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//                imageToBeLoaded.setImageBitmap(bitmap)
//            }
//            catch (e: IOException){
//                e.printStackTrace()
//            }
//        }
//        else if(resultCode== RESULT_CANCELED){
//            Toast.makeText(applicationContext,"Cancelled",Toast.LENGTH_LONG).show()
//        }
        if(resultCode == RESULT_OK){
            when(requestCode){
                CAMERA_REQUEST_CODE ->{
                    bitmap = data?.extras?.get("data") as Bitmap
                    imageToBeLoaded.setImageBitmap(bitmap)
                }
                GALLERY_REQUEST_CODE ->{
                    data!!.data.also { imageUri = it }
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
                    imageToBeLoaded.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
    }
}