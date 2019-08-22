package com.dlucci.consultantcamerax

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import java.io.File
import kotlinx.android.synthetic.main.activity_image_details.image
import kotlinx.android.synthetic.main.activity_image_details.size
import kotlinx.android.synthetic.main.activity_image_details.trash

class ImageDetailsActivity : AppCompatActivity() {

    var path: String? = null

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