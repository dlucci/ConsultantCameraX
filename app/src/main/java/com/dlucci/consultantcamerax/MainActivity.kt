package com.dlucci.consultantcamerax

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

private const val REQUEST_CODE_PERMISSIONS = 42

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)//, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

class MainActivity : AppCompatActivity(), LifecycleOwner {

    lateinit var PATH : File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PATH = externalMediaDirs.first()

        preview.setOnClickListener {
            var i = Intent(this, ImageActivity::class.java)
            startActivity(i)
        }

        if(allPermissionsGranted()) {
            viewFinder.post {
                startCamera()
                populatePreview()
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun populatePreview() {
        var file = PATH.list()
        file.reverse()
        preview.setImageDrawable(Drawable.createFromPath(PATH.path + "/" + file[0]))
    }

    private fun startCamera() {
        CameraX.bindToLifecycle(this, createPreview(), createImageCapture())
    }

    private fun createImageCapture(): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            //TODO:  Reduce resolution once the rest of the app is working
            setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            setTargetRotation(Surface.ROTATION_90)

        }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)
        capture.setOnClickListener{
            val file = File(PATH,
                "${System.currentTimeMillis()}.jpg")
            Log.d("EIFLE", file.absolutePath)
            imageCapture.takePicture(file,
                object : ImageCapture.OnImageSavedListener{
                    override fun onImageSaved(file: File) {
                        Snackbar.make(viewFinder, "Image Saved!", Snackbar.LENGTH_LONG).show()
                        populatePreview()
                    }

                    override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                        Snackbar.make(viewFinder, message, Snackbar.LENGTH_LONG).show()
                    }
                })
        }

        return imageCapture
    }

    private fun createPreview(): Preview {
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            setTargetResolution(
                Size(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            )
            setTargetRotation(Surface.ROTATION_0)
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {
            val parent = viewFinder.parent as ViewGroup

            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        return preview
    }

    private fun updateTransform() {
        val matrix = Matrix()

        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 90
            Surface.ROTATION_90 -> 0
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        viewFinder.setTransform(matrix)
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
    override fun onClick(view: View?) {
        ActivityCompat.requestPermissions(
            activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

}


