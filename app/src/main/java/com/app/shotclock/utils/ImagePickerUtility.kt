package com.app.dategame.utils


import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.provider.Settings
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.shotclock.R
import com.app.shotclock.base.BaseFragment
import com.intuit.sdp.BuildConfig.APPLICATION_ID
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


abstract class ImagePickerUtility : BaseFragment() {

    val REQUEST_CODE = 100
    private val GALLERY_PIC_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE = 102
    var mVideoDialog: Boolean = false
    var mPhotoFile: File? = null
    private val GALLERY_VIDEO_REQUEST_CODE = 1001
    private val REQUEST_VIDEO_CAPTURE = 1002

    var imageAbsolutePath: File? = null
    private var mActivity: Activity? = null
    private var mCode = 0


    @RequiresApi(Build.VERSION_CODES.M)
    open fun getImage(activity: Activity, code: Int, videoDialog: Boolean) {
        mActivity = activity
        mCode = code
        mVideoDialog = videoDialog

        //*****videoDialog -> put false for pick the Image.*****
        //*****videoDialog -> put true for pick the Video.*****

        if (!cameraPermission(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                checkPermissionDenied(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                checkPermissionDenied(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            } else {
                requestPermission()
            }
        } else {
            imageDialog()

        }
    }

    private fun imageDialog() {
        val dialog = Dialog(mActivity!!)
        dialog.setContentView(R.layout.camera_gallery_popup)
//        dialog.window!!.attributes.windowAnimations = R.style.Theme_Dialog
        val window = dialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val camera = dialog.findViewById<TextView>(R.id.tvCamera)
        val cancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        val gallery = dialog.findViewById<TextView>(R.id.tvGallery)
        cancel.setOnClickListener { dialog.dismiss() }

        camera.setOnClickListener {
            dialog.dismiss()
            if (mVideoDialog) {
                captureVideo(mActivity!!)
            } else {
                captureImage(mActivity!!)
            }

        }

        gallery.setOnClickListener {
            dialog.dismiss()
            if (mVideoDialog) {
                openGalleryForVideo(mActivity!!)
            } else {
                openGallery(mActivity!!)
            }
        }
        dialog.show()
    }

    open fun captureImage(activity: Activity) {
        mActivity = activity
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context?.packageManager!!) != null) { // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    APPLICATION_ID + ".provider", photoFile
                )
                mPhotoFile = photoFile
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }
        }
    }

    open fun captureVideo(activity: Activity) {
        mActivity = activity
        val i = Intent(Intent.ACTION_PICK, Images.Media.EXTERNAL_CONTENT_URI)
        i.type = "video/*"
        startActivityForResult(i, REQUEST_VIDEO_CAPTURE)

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
//        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {

            imageAbsolutePath = this
        }
    }


    open fun openGallery(activity: Activity) {
        mActivity = activity
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intent, "Select a File"),
            GALLERY_PIC_REQUEST_CODE
        )
    }

    open fun openGalleryForVideo(activity: Activity) {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            GALLERY_VIDEO_REQUEST_CODE
        )

    }

    @Throws(IOException::class)
    private fun createImageFileFromCamera(): File? { // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    private fun cameraPermission(permissions: Array<String>): Boolean {
        return ContextCompat.checkSelfPermission(
            mActivity!!,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            mActivity!!,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
    }

    open fun requestPermission() {
        ActivityCompat.requestPermissions(
            mActivity!!, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), REQUEST_CODE
        )
    }

    private fun checkPermissionDenied(permissions: Array<out String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permissions[0])) {
                val mBuilder = AlertDialog.Builder(mActivity)
                val dialog: AlertDialog =
                    mBuilder.setTitle(R.string.alert).setMessage(R.string.permissionRequired)
                        .setPositiveButton(
                            R.string.ok
                        ) { dialog, which -> requestPermission() }
                        .setNegativeButton(
                            R.string.cancel
                        ) { dialog, which ->

                        }.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(
                            mActivity!!, R.color.red
                        )
                    )
                }
                dialog.show()
            } else {
                val builder = AlertDialog.Builder(mActivity)
                val dialog: AlertDialog =
                    builder.setTitle(R.string.alert).setMessage(R.string.permissionRequired)
                        .setCancelable(
                            false
                        )
                        .setPositiveButton(R.string.openSettings) { dialog, which ->
                            //finish()
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", "com.pledge.pledgeofpeace", null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }.create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(
                            mActivity!!, R.color.red
                        )
                    )
                }
                dialog.show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImage(mActivity!!, mCode, mVideoDialog)
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                checkPermissionDenied(permissions)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result = CropImage.getActivityResult(data)
//            if (resultCode == RESULT_OK) {
//                val resultUri = result.uri
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                val error = result.error
//            }
//        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            val uri = Uri.fromFile(File(mPhotoFile?.absolutePath))

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            val file = File(uri.path)

            var imgPath = getPath(uri)

            selectedImage(file, imgPath)

        } else if (requestCode == GALLERY_PIC_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                if (data?.data != null) {
                    val file = saveImage(
                        MediaStore.Images.Media.getBitmap(
                            context?.contentResolver,
                            data?.data
                        ), requireActivity()
                    )!!

                    var imgPath = getPath(data?.data)


                    selectedImage(file, imgPath)

                }
            } catch (e: Exception) {
            }


        }
    }


    abstract fun selectedImage(imagePath: File?, path: String?)


    fun saveImage(myBitmap: Bitmap, ctx: Activity): File? {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        val imageDirectory = File(ctx.getExternalFilesDir(null).toString() + "IMAGE_DIRECTORY")
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs()
        }
        try {
            val f = File(
                imageDirectory,
                Calendar.getInstance().timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(ctx, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            return f

        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return null
    }


    open fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }


    // UPDATED!
    open fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val cursor = context?.contentResolver?.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else
            return null
    }


    open fun getPathFromUri(context: Context?, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(requireContext(), contentUri, selection, selectionArgs)
            }
        }
        return null
    }

    open fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    open fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }

        }
        finally

        {
            cursor?.close()
        }

        return null
    }

}
