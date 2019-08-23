package com.dlucci.consultantcamerax

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import java.io.File

class GalleryAdapter() : RecyclerView.Adapter<ImageViewHolder>() {

    var files: Array<String>? = null

    lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
        files = context.path().list()
        files?.rSort()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        var view = parent.context.inflate(parent, R.layout.gallery_row)

        return ImageViewHolder(view)
    }

    override fun getItemCount() = files?.count() ?: 0

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(files?.get(position) ?: "")
    }
}

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.image)

    var fullPath: String? = null

    fun bind(string: String?) {
        var fullPath = imageView.context.path().path + "/" + string
        this.fullPath = fullPath
        imageView.load(File(fullPath)) {
            placeholder(R.mipmap.ic_launcher)
            error(R.mipmap.ic_launcher_round)
        }

        val bundle = Bundle()
        bundle.putString("path", fullPath)
        imageView.setNavigate(R.id.action_to_details, bundle)
    }
}