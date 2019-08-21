package com.dlucci.consultantcamerax

const val KB = 1000.0
const val MB = 10000.0

fun Long.toKb(param: Long): String {
    if ((param / KB) >= 1000)
        return param.toMb(param)

    return String.format("%sKB", param / KB)
}

fun Long.toMb(param: Long): String = String.format("%sMB", param / MB)
