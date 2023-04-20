package com.example.parentsconnect

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import kotlin.math.min

class ChangePictureFragment : Fragment() {

    private val OPEN_GALLERY = 100

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_picture, container, false)

        imageView = view.findViewById(R.id.changePicView)

        val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("userId", 0)

        val db = DatabaseHelper(requireContext())
        val imageByteArray = db.getImage(userId)
        if(imageByteArray == null) {
            val drawable = resources.getDrawable(R.drawable.ic_baseline_account_circle_24, null)
            imageView.setImageDrawable(drawable)
        } else {
            val imageBitMap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

//            // Calculate the diameter of the circle and the radius of the image
//            val diameter = min(imageBitMap.width, imageBitMap.height)
//            val radius = diameter / 2f
//
//
//            val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
//
//            // Create a Canvas object and draw the scaled bitmap image onto it
//            val canvas = Canvas(output)
//            canvas.drawCircle(radius, radius, radius, Paint().apply { isAntiAlias = true })
//            canvas.drawBitmap(imageBitMap, (diameter - imageBitMap.width) / 2f, (diameter - imageBitMap.height) / 2f, Paint().apply { isAntiAlias = true; xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) })


            imageView.setImageBitmap(imageBitMap)
        }
//        val imageUri = db.getProfilePictureUri(userId)

//        if(imageUri == null) {
//            val drawable = resources.getDrawable(R.drawable.ic_baseline_account_circle_24, null)
//            imageView.setImageDrawable(drawable)
//        } else {
//            val uri = Uri.parse(imageUri)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            val persistableUriPermission = intent.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//            if (persistableUriPermission != 0) {
//                // The URI has a persistable permission grant, so no need to request permissions
//                imageView.setImageURI(uri)
//            } else {
//                // The URI does not have a persistable permission grant, so request permissions
////                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
////                requireActivity().contentResolver.takePersistableUriPermission(uri, takeFlags)
////                imageView.setImageURI(uri)
//            }
//            imageView.setImageURI(Uri.parse(imageUri))
//        }

        val changePicBtn = view.findViewById<Button>(R.id.changePicture)
        val removePicBtn = view.findViewById<Button>(R.id.removePicture)
        val backButton = view.findViewById<ImageView>(R.id.backBtnCPic)

        changePicBtn.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            startActivityForResult(intent, OPEN_GALLERY)
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), OPEN_GALLERY)
        }

        removePicBtn.setOnClickListener {
            db.removeImage(userId)
            imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        }

        backButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == OPEN_GALLERY && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
//            requireActivity().contentResolver.takePersistableUriPermission(
//                imageUri!!,
//                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//            )
            val imageToStore = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)

            // Calculate the diameter of the circle and the radius of the image
            val diameter = min(imageToStore.width, imageToStore.height)
            val radius = diameter / 2f


            val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)

            // Create a Canvas object and draw the scaled bitmap image onto it
            val canvas = Canvas(output)
            canvas.drawCircle(radius, radius, radius, Paint().apply { isAntiAlias = true })
            canvas.drawBitmap(imageToStore, (diameter - imageToStore.width) / 2f, (diameter - imageToStore.height) / 2f, Paint().apply { isAntiAlias = true; xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) })


            imageView.setImageBitmap(output)

            val uriString = imageUri.toString()

            val sharedPref = requireActivity().getSharedPreferences("parentsConnectPref", Context.MODE_PRIVATE)
            val userId = sharedPref.getInt("userId", 0)

            val db = DatabaseHelper(requireContext())
            db.insertImage(userId, output)
//            imageView.setImageURI(imageUri)
//            imageView.setImageURI(Uri.parse(str))
        }
    }

}