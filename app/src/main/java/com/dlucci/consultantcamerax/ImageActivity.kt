package com.dlucci.consultantcamerax

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = ImageAdapter(this)
    }
}
