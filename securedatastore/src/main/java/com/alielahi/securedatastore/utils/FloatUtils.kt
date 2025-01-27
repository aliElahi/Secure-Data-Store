package com.alielahi.securedatastore.utils

import java.nio.ByteBuffer

fun Float.toByteArray(): ByteArray {
    val buffer = ByteBuffer.allocate(Float.SIZE_BYTES)
    buffer.putFloat(this)
    buffer.rewind()
    return buffer.array()
}

fun ByteArray.toFloat(): Float {

    val buffer = ByteBuffer.allocate(Float.SIZE_BYTES)
    val dataByte = this.copyOfRange(0, Float.SIZE_BYTES)
    buffer.put(dataByte)
    buffer.rewind()
    return buffer.float

}