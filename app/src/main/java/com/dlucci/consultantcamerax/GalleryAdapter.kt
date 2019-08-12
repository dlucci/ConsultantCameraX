package com.dlucci.consultantcamerax

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(var context : Context) : RecyclerView.Adapter<ImageViewHolder>() {

    var files : Array<String>? = null

    init {
        files = context.externalMediaDirs.first().list()
        files?.reverse()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(context), parent)
    }

    override fun getItemCount() = files?.count() ?: 0


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(files?.get(position) ?: "")
    }

}

class ImageViewHolder(inflator : LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflator.inflate(R.layout.gallery_row, parent, false)) {
    var imageView : ImageView

    init {
        imageView = itemView.findViewById(R.id.image)
    }

    fun bind(string : String) {
        var fullPath = imageView.context.externalMediaDirs.first().path + "/" + string
        imageView.setImageDrawable(Drawable.createFromPath(fullPath))
    }


}
