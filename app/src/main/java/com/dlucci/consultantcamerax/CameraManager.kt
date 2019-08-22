package com.dlucci.consultantcamerax

import android.content.Context
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.util.Rational
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import java.io.File

class CameraManager(var context: Context) {

    fun startCamera(data: CameraData) {
        CameraX.bindToLifecycle(data.lifecycleOwner, createPreview(data.textureView),
            createImageCapture(data))
    }

    private fun createPreview(viewFinder: TextureView?): Preview {

        val metrics = DisplayMetrics().real(viewFinder ?: View(context))
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(screenAspectRatio)
        }.build()

        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {
            val parent = viewFinder?.parent as ViewGroup

            parent.removeView(viewFinder)
            parent.addView(viewFinder, 0)

            viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform(viewFinder)
        }

        return preview
    }

    private fun updateTransform(viewFinder: TextureView?) {
        val matrix = Matrix()

        val centerX = (viewFinder?.width ?: 0) / 2f
        val centerY = (viewFinder?.height ?: 0) / 2f

        val rotationDegrees = when (viewFinder?.display?.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }

        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        viewFinder.setTransform(matrix)
    }

    fun populatePreview(preview: ImageView) {
        var file = context.path().list()
        if (file.isNotEmpty()) {
            file.reverse()
            preview.load(File((context.path().path + "/" + file[0])))
        } else {
            // REALLY don't love this icon
            preview.load(context.getImage(android.R.drawable.ic_secure))
        }
    }

    private fun createImageCapture(data: CameraData): ImageCapture {
        val metrics = DisplayMetrics().real(data.textureView ?: View(context))
        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
            setTargetAspectRatio(screenAspectRatio)
            // TODO:  Reduce resolution once the rest of the app is working
            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
        }.build()

        val imageCapture = ImageCapture(imageCaptureConfig)

        data.capture?.setOnClickListener {
            val file = File(context.path(), "${System.currentTimeMillis()}.jpg")
            imageCapture.takePicture(file,
                object : ImageCapture.OnImageSavedListener {
                    override fun onImageSaved(file: File) {
                        Snackbar.make(data.textureView ?: View(context),
                            "Image Saved!", Snackbar.LENGTH_LONG).show()
                        populatePreview(data.preview)
                    }

                    override fun onError(
                        useCaseError: ImageCapture.UseCaseError,
                        message: String,
                        cause: Throwable?
                    ) {
                        Snackbar.make(data.textureView ?: View(context),
                            message, Snackbar.LENGTH_LONG).show()
                    }
                })
        }

        return imageCapture
    }
}