package com.dlucci.consultantcamerax

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_image_details.*
import java.io.File

class ImageDetailsActivity : AppCompatActivity() {

    var path : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        path = intent.extras?.getString("details", "")

        image.setImageDrawable(Drawable.createFromPath(path))

        var file = File(path ?: "")

        var fileSize = file.length()/1000.0

        size.text = String.format("%skB",fileSize.toString())

        trash.setOnClickListener {
            file.delete()
            finish()
        }
    }

}
