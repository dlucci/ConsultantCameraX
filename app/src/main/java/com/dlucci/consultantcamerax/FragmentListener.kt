package com.dlucci.consultantcamerax

import androidx.lifecycle.LifecycleOwner

interface FragmentListener {
    fun allPermissionsGranted(): Boolean

    fun getRequiredPermissions(): Array<String>

    fun getCodePermissions(): Int

    fun getLifecycleOwner(): LifecycleOwner
}