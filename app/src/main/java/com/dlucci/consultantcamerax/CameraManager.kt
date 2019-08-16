package com.dlucci.consultantcamerax

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.camera.core.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class CameraManager(var context : Context) {

    lateinit var PATH : File

    init {
        PATH = context.externalMediaDirs.first()
    }

    fun startCamera(lifecycleOwner: LifecycleOwner, capture: ImageButton, textureView: TextureView, preview : ImageView ) {
        CameraX.bindToLifecycle(lifecycleOwner, createPreview(textureView), createImageCapture(capture, textureView, preview))
    }

    private fun createPreview(viewFinder: TextureView): Preview {

        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(screenAspectRatio)
            setTargetResolution(
                Size(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
            )
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {
            val parent = viewFinder.parent as ViewGroup

            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform(viewFinder)
        }

        return preview
    }

    private fun updateTransform(viewFinder: TextureView) {
        val matrix = Matrix()

        val centerX = viewFinder.width / 2f
        val centerY = viewFinder.height / 2f

        val rotationDegrees = when(viewFinder.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        viewFinder.setTransform(matrix)
    }

    fun populatePreview(preview : ImageView) {
        var file = PATH.list()
        if(file.isNotEmpty()) {
            file.reverse()
            preview.setImageDrawable(Drawable.createFromPath(PATH.path + "/" + file[0]))
        } else {
            //REALLY don't love this icon
            preview.setImageDrawable(context.resources.getDrawable(android.R.drawable.ic_secure, null))
        }
    }

    private fun createImageCapture(capture: ImageButton, viewFinder : TextureView, preview: ImageView): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(Rational(1, 1))
            //TODO:  Reduce resolution once the rest of the app is working
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)

        capture.setOnClickListener{
            val file = File(PATH, "${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file,
                object : ImageCapture.OnImageSavedListener{
                    override fun onImageSaved(file: File) {
                        Snackbar.make(viewFinder, "Image Saved!", Snackbar.LENGTH_LONG).show()
                        populatePreview(preview)
                    }

                    override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                        Snackbar.make(viewFinder, message, Snackbar.LENGTH_LONG).show()
                    }
                })
        }

        return imageCapture
    }
}