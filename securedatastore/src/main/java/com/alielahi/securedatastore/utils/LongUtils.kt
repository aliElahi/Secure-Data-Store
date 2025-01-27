package com.alielahi.securedatastore.utils

import java.nio.ByteBuffer

fun Long.toByteArray(): ByteArray {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    buffer.putLong(this)
    buffer.rewind()
    return buffer.array()
}

fun ByteArray.toLong(): Long {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    val dataByte = this.copyOfRange(0, Long.SIZE_BYTES)
    buffer.put(dataByte)
    buffer.rewind()
    return buffer.long

}