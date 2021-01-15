package ru.startandroid.develop.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_photo_picker.*

const val REQUEST_PHOTO = 100
const val SELECT_PHOTO = 100

class PhotoPickerActivity : AppCompatActivity(), PickerView.OnPickerListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_picker)
        pickerView.setListener(this)
    }

    private fun onPermissionDenied() {
        Toast.makeText(this, getString(R.string.denied), Toast.LENGTH_SHORT).show()
    }

    private fun onPickerClicked(isAllowed: Boolean) {
        if (!isAllowed) {
            makeRequestWritePermissions(REQUEST_PHOTO)
        } else {
            startPickPhoto()
        }
    }

    private fun setUpPhotoError() =
            Toast.makeText(this, getString(R.string.error_photo_load), Toast.LENGTH_SHORT).show()

    private fun makeRequestWritePermissions(requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                pickerView.setImageUri(it)
            } ?: setUpPhotoError()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PHOTO -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    onPermissionDenied()
                } else {
                    startPickPhoto()
                }
            }
        }
    }

    private fun startPickPhoto() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(
                photoPickerIntent,
                SELECT_PHOTO
        )
    }

    override fun onPickImage() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        onPickerClicked(permission == PackageManager.PERMISSION_GRANTED)
    }
}