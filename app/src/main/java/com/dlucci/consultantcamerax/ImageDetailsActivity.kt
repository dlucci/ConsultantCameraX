package com.dlucci.consultantcamerax

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import coil.api.load

import kotlinx.android.synthetic.main.activity_image_details.*
import java.io.File

class ImageDetailsActivity : AppCompatActivity() {

    var path : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        path = intent.extras?.getString("details", "")

        image.load(File(path))

        var file = File(path ?: "")

        var fileSize = file.length().toKb(file.length())

        size.text = fileSize

        trash.setOnClickListener {
            file.delete()
            finish()
        }
    }

}
