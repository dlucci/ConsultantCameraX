package com.dlucci.consultantcamerax

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.capture
import kotlinx.android.synthetic.main.activity_main.preview
import kotlinx.android.synthetic.main.activity_main.viewFinder

private const val REQUEST_CODE_PERMISSIONS = 42

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class MainActivity : AppCompatActivity(), LifecycleOwner, CameraXInterface {
    override fun updateApdater() {
    }

    lateinit var mCamera: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preview.setOnClickListener {
            var i = Intent(this, GalleryActivity::class.java)
            startActivity(i)
        }

        mCamera = CameraManager(this)

        if (allPermissionsGranted()) {
            viewFinder.post {
                startCamera()
                mCamera.populatePreview(preview)
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onResume() {
        super.onResume()

        mCamera.populatePreview(preview)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Snackbar.make(viewFinder, "You did not grant all permissions", Snackbar.LENGTH_LONG)
                    .setAction("Request", PermissionListener(this))
                    .show()
            }
        }
    }

    private fun startCamera() {
        mCamera.startCamera(this, capture, viewFinder, preview)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

class PermissionListener(var activity: Activity) : View.OnClickListener {
    override fun onClick(view: View?) {
        ActivityCompat.requestPermissions(
            activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }
}
