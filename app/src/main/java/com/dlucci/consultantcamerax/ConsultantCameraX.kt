package com.dlucci.consultantcamerax

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class ConsultantCameraX : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())
    }
}
