package com.alielahi.securedatastore.utils

import java.nio.ByteBuffer

fun Int.toByteArray(): ByteArray {
    val buffer = ByteBuffer.allocate(Int.SIZE_BYTES)
    buffer.putInt(this)
    buffer.rewind()
    return buffer.array()
}

fun ByteArray.toInt(): Int {

    val buffer = ByteBuffer.allocate(Int.SIZE_BYTES)
    val dataByte = this.copyOfRange(0, Int.SIZE_BYTES)
    buffer.put(dataByte)
    buffer.rewind()
    return buffer.int

}




