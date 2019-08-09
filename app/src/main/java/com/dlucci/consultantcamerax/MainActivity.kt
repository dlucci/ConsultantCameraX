package com.dlucci.consultantcamerax

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_CODE_PERMISSIONS = 42

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class MainActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(allPermissionsGranted()) {
            viewFinder.post { startCamera()}
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        //TODO add this function
    }

    private fun updateTransform() {
        //TODO add this function
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissionsGranted()){
                viewFinder.post { startCamera() }
            } else {
                Snackbar.make(viewFinder, "You did not grant all permissions", Snackbar.LENGTH_LONG)
                    .setAction("Request", PermissionListener(this))
                    .show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

class PermissionListener(var activity : Activity) : View.OnClickListener {
    override fun onClick(p0: View?) {
        ActivityCompat.requestPermissions(
            activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

}


