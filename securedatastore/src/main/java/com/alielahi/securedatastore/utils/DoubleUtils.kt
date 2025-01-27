package com.alielahi.securedatastore.utils

import java.nio.ByteBuffer

fun Double.toByteArray(): ByteArray {
    val buffer = ByteBuffer.allocate(Double.SIZE_BYTES)
    buffer.putDouble(this)
    buffer.rewind()
    return buffer.array()
}

fun ByteArray.toDouble(): Double {

    val buffer = ByteBuffer.allocate(Double.SIZE_BYTES)
    val dataByte = this.copyOfRange(0, Double.SIZE_BYTES)
    buffer.put(dataByte)
    buffer.rewind()
    return buffer.double

}