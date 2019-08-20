package com.dlucci.consultantcamerax

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.gallery_row.view.*
import java.io.File

class GalleryAdapter() : RecyclerView.Adapter<ImageViewHolder>() {

    var files : Array<String>? = null

    lateinit var context: Context

    constructor(context : Context): this() {
        this.context = context
        files = context.externalMediaDirs.first().list()
        files?.reverse()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount() = files?.count() ?: 0


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(files?.get(position) ?: "")
    }

}

class ImageViewHolder(inflator : LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflator.inflate(R.layout.gallery_row, parent, false)) {
    var imageView : ImageView = itemView.findViewById(R.id.image)

    var fullPath : String? = null

    init {
        imageView.setOnClickListener {
            var i = Intent(itemView.context, ImageDetailsActivity::class.java)
            i.putExtra("details", fullPath)
            imageView.context.startActivity(i)
        }
    }

    fun bind(string : String?) {
        var fullPath = imageView.context.externalMediaDirs.first().path + "/" + string
        this.fullPath = fullPath
        imageView.load(File(fullPath))
    }

}
