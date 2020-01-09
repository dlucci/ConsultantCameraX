package com.dlucci.consultantcamerax

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

const val KB = 1000.0
const val MB = 10000.0

fun Long.toKb(): String {
    if ((this / KB) >= 1000)
        return this.toMb()

    return String.format("%sKB", this / KB)
}

fun Long.toMb(): String = String.format("%sMB", this / MB)

fun Context.getImage(id: Int): Drawable = this.resources.getDrawable(id, null)

fun DisplayMetrics.real(view: View) =
    DisplayMetrics().also { view.display.getRealMetrics(it) }

fun Context.inflate(parent: ViewGroup, id: Int): View {
    var layoutInflater: LayoutInflater =
        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    return layoutInflater.inflate(id, parent, false)
}

fun Context.path() = this.externalMediaDirs.first()

fun View.setNavigate(action: Int, bundle: Bundle) =
    this.setOnClickListener(Navigation.createNavigateOnClickListener(action, bundle))

fun Array<out String>.rSort() {
    this.sort()
    this.reverse()
}