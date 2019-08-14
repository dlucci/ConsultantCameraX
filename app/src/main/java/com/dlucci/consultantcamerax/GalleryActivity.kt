package com.dlucci.consultantcamerax

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = ImageAdapter(this)
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter?.notifyDataSetChanged()
    }
}
