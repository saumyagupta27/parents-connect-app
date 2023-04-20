package com.example.parentsconnect

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File


class PostNoticeActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1
    private val REQUEST_DOCUMENTS = 200

    private val ChannelId = "channel_id"
    private val notificationId = 100

    private lateinit var removeFileBtn: Button
    private lateinit var fileNameTextView: TextView

    private var uri : Uri? = null
        set(value) {
            field = value
            onUriChanged()
        }

    private var fileName: String = ""
        set(value) {
            field = value
            onFileNameChanged()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_notice)

        val titleEditText = findViewById<EditText>(R.id.noticeTitle)
        val contentEditText = findViewById<EditText>(R.id.noticeText)
        val attachFileBtn = findViewById<Button>(R.id.addFileBtn)
        val isImportantCheckbox = findViewById<CheckBox>(R.id.isImportantCheckbox)
        val submitBtn = findViewById<Button>(R.id.submitNoticeBtn)
        val backBtn = findViewById<ImageView>(R.id.backBtnNP)
        removeFileBtn = findViewById(R.id.removeFileBtn)
        fileNameTextView = findViewById(R.id.fileNameTV)

        val db  = DatabaseHelper(this)

        if(uri == null) {
            changeVisibility(false)
        } else {
            changeVisibility(true)
        }

        attachFileBtn.setOnClickListener {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "*/*"
//            startActivityForResult(intent, 1)

            if(Build.VERSION.SDK_INT >= 23) {
                if(checkPermission()) {
                    filePicker()
                } else {
                    requestPermission()
                }
            }
            else {
                filePicker()
            }

        }

        removeFileBtn.setOnClickListener {
            this.uri = null
            this.fileName = ""
        }

        submitBtn.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            if(title == "" || content  == "") {
                Toast.makeText(this, "The title and content fields cannot be left empty!", Toast.LENGTH_SHORT).show()
            } else {
                val isImportant = isImportantCheckbox.isChecked
                val documentUri = this.uri
//                Toast.makeText(this, "File attached - $documentUri", Toast.LENGTH_SHORT).show()

                val res = db.addNotice(title, content, isImportant, documentUri)
                if(res) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    // at the end make uri null again
                    this.uri = null

                    // erase all text from EditText fields
                    titleEditText.setText("")
                    contentEditText.setText("")

                    // notification if notice is important
                    if(isImportantCheckbox.isChecked) {
                        // create notification channel
                        createNotificationChannel()

                        // send notification
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
                        val bigTextStyle = NotificationCompat.BigTextStyle()
                            .bigText(content)
                        val builder = NotificationCompat.Builder(this, ChannelId)
                            .setSmallIcon(R.drawable.ic_notification_foreground)
                            .setContentTitle(title)
                            .setContentText(content)
                            .setStyle(bigTextStyle)
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)

                        with(NotificationManagerCompat.from(this)) {
                            notify(notificationId, builder.build())
                        }
                    }

                    isImportantCheckbox.isChecked = false
                }
            }

        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onUriChanged() {
        if(this.uri == null) {
            changeVisibility(false)
            fileNameTextView.text = ""
        } else {
            changeVisibility(true)
            fileNameTextView.text = this.fileName
        }
    }

    private fun onFileNameChanged() {
        if(this.fileName != "") {
            fileNameTextView.text = this.fileName
        }
    }

    private fun changeVisibility(isVisible: Boolean) {
        if(!isVisible) {
            removeFileBtn.visibility = View.GONE
            fileNameTextView.visibility = View.GONE
        } else {
            removeFileBtn.visibility = View.VISIBLE
            fileNameTextView.visibility = View.VISIBLE
        }
    }

    private fun filePicker() {
        Toast.makeText(this, "File picker called", Toast.LENGTH_SHORT).show()
        val openDocuments = Intent(Intent.ACTION_OPEN_DOCUMENT)
        openDocuments.type = "application/pdf"
        startActivityForResult(openDocuments, REQUEST_DOCUMENTS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_DOCUMENTS && resultCode == Activity.RESULT_OK) {
            this.uri = data?.data!!
            val selectedDocumentUri: Uri? = data?.data
            selectedDocumentUri?.let {
                contentResolver.takePersistableUriPermission(
                    selectedDocumentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val viewDocumentIntent = Intent(Intent.ACTION_VIEW)
                viewDocumentIntent.setDataAndType(selectedDocumentUri, "application/pdf")
                viewDocumentIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                startActivity(viewDocumentIntent)
            }

            val cursor = contentResolver.query(data.data!!, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                Log.d("Filename: ", "$displayName")
                this.fileName = displayName.toString()
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri){
        var filePath: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)

        try {
            if (cursor != null && cursor.moveToFirst()) {
                val displayName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                Log.d("name: ", "$displayName")
                this.fileName = displayName
                val pdfFile = File(Environment.getExternalStorageDirectory(), "$displayName") //File path
                val path = Uri.fromFile(pdfFile)
                val fileUri = FileProvider.getUriForFile(
                    this,
                    getPackageName().toString() + ".fileprovider",
                    pdfFile
                )

                val objIntent = Intent(Intent.ACTION_VIEW)
                objIntent.setDataAndType(fileUri, "application/pdf")
                objIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(objIntent) //Staring the pdf viewer
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }

    private fun requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))  {
            Toast.makeText(this, "Please give permission", Toast.LENGTH_SHORT).show()
        }
        else {
//            val PERMISSION_REQUEST_CODE = 1
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkPermission() : Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifTitle = "Notification title"
            val notifDesciption = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ChannelId, notifTitle, importance).apply {
                description = notifDesciption
            }
            // register channel with system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}