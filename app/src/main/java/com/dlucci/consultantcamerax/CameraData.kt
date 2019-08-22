package com.dlucci.consultantcamerax

import android.view.TextureView
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner

data class CameraData(
    var lifecycleOwner: LifecycleOwner? = null,
    var capture: ImageButton? = null,
    var textureView: TextureView? = null,
    var preview: ImageView
)