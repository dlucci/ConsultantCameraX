package com.dlucci.consultantcamerax

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        recyclerView.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    }

    override fun onResume() {
        super.onResume()

        recyclerView.adapter = GalleryAdapter(this)
    }
}
