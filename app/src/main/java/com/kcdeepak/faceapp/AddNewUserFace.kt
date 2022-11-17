package com.kcdeepak.faceapp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.BadPaddingException


class AddNewUserFace : AppCompatActivity() {
    private lateinit var myViewModel:MyViewModel
    private lateinit var imageToBeLoaded: ImageView
    private lateinit var saveButton:Button
    private var imageUri: Uri? = null
    private lateinit var editTextName:EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextAddress: EditText
    private var bitmap:Bitmap?=null

    private val contractForGallery = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        //imageToBeLoaded.setImageURI(it)
        bitmap = Images.Media.getBitmap(contentResolver, it)

        imageUri = getImageUriFromBitmap(applicationContext,bitmap!!)
        Glide.with(this)
            .load(imageUri)
            .into(imageToBeLoaded)
        Log.d("ImageUriToString", imageUri.toString())
    }

    private val contractForCamera = registerForActivityResult(ActivityResultContracts.TakePicture()){
        //imageToBeLoaded.setImageURI(imageUri)
        Glide.with(this)
            .load(imageUri)
            .into(imageToBeLoaded)
        Log.d("ImageUriToString", imageUri.toString())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user_face)

        initialMyUI()
    }

    override fun onResume() {
        super.onResume()
        imageToBeLoaded.setOnClickListener {

            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select an Option")
            val pictureDialogItem = arrayOf("Open Gallery","Open Camera")
            pictureDialog.setItems(pictureDialogItem){
                    _, which ->
                when(which){
                    0->checkGalleryPermission()
                    1->checkCameraPermission()
                }
            }
            pictureDialog.show()
        }

        saveButton.setOnClickListener {
            //val base64String = convertToBase64String(bitmap as Bitmap)
            if(editTextName.text.toString().isNotEmpty() && editTextAddress.text.toString().isNotEmpty()
                && editTextPhone.text.toString().isNotEmpty() && imageUri!=null){
                val encryptDecryptName = EncryptDecrypt()
                val name = encryptDecryptName.encrypt(editTextName.text.toString().toByteArray())
                val nameIV = encryptDecryptName.encryptCipher.iv
                /*-------------------------------------------------------------*/
                val encryptDecryptPhone = EncryptDecrypt()
                val phone = encryptDecryptPhone.encrypt(editTextPhone.text.toString().toByteArray())
                val phoneIV = encryptDecryptPhone.encryptCipher.iv
                /*-------------------------------------------------------------*/
                val encryptDecryptAddress = EncryptDecrypt()
                val address = encryptDecryptAddress.encrypt(editTextAddress.text.toString().toByteArray())
                val addressIV = encryptDecryptAddress.encryptCipher.iv
                /*-------------------------------------------------------------*/
                val encryptDecryptImageUri = EncryptDecrypt()
                val uri = encryptDecryptImageUri.encrypt(imageUri.toString().toByteArray())
                val imageUriIV = encryptDecryptImageUri.encryptCipher.iv

                val userFace =
                    UserFace(
                        0,
                        name,
                        nameIV,
                        phone,
                        phoneIV,
                        address,
                        addressIV,
                        uri,
                        imageUriIV
                    )
                myViewModel.insertFaceUser(userFace)
                try {
                    val encryptDecrypt = EncryptDecrypt()
                    val cipherText = encryptDecrypt.encrypt(editTextName.text.toString().toByteArray())
                    Log.d("Encrypt", "$cipherText")

                    val plainText = String(encryptDecrypt.decrypt(cipherText,encryptDecrypt.encryptCipher.iv))
                    Log.d("Decrypt", "$plainText")
                }catch (e:BadPaddingException){
                    e.printStackTrace()
                }
                intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this@AddNewUserFace,"Please fill all the fields",Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun checkCameraPermission() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE)
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
            )
            .onSameThread()
            .check()
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
                dialog,_ -> dialog.dismiss()
            }.show()
    }

    private fun camera() {
//        intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent,CAMERA_REQUEST_CODE)
        imageUri = createImageUri()
        contractForCamera.launch(imageUri)
    }
    private fun gallery() {
//        intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent,0)

        contractForGallery.launch(arrayOf("image/*"))
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == RESULT_OK){
//            if(requestCode==0) {
//                data!!.data.also { imageUri = it }
//                Log.d("ImageUriToString", "onActivityResult: ${imageUri.toString()}")
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
//                imageToBeLoaded.setImageBitmap(bitmap)
//            }
//        }
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
    }

    private fun initialMyUI(){
        myViewModel = MyViewModel(this)
        imageToBeLoaded = findViewById(R.id.imageToBeAdded)
        saveButton = findViewById(R.id.saveButton)
        editTextName = findViewById(R.id.editTextName)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextAddress = findViewById(R.id.editTextAddress)
    }
    private fun createImageUri():Uri? {
        val time = SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())
        val image = File(applicationContext.filesDir, "IMG_${time}.png")
        return FileProvider.getUriForFile(applicationContext,
            "com.kcdeepak.faceapp.fileprovider",
            image)
    }


    private fun getImageUriFromBitmap(context: Context,bitmap: Bitmap):Uri{
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val filePath = Images.Media.insertImage(context.contentResolver,bitmap,"File",null)
        return Uri.parse(filePath.toString())
    }

//    fun convertToBase64String(bitmap: Bitmap):String{
//        val byteBuffer = ByteBuffer.allocate(bitmap.height * bitmap.rowBytes)
//        bitmap.copyPixelsToBuffer(byteBuffer)
//        val byteArray = byteBuffer.array()
//        return encodeToString(byteArray, DEFAULT)
//    }

//    private fun base64StringFromCipherText(cipherText:ByteArray):String{
//        return Base64.getEncoder().encodeToString(cipherText)
//    }
}
