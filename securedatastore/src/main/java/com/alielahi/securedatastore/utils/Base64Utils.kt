package com.alielahi.securedatastore.utils

import android.util.Base64

fun ByteArray.getBase64String(): String {
    return Base64.encodeToString(this, Base64.DEFAULT)
}

fun String?.fromBase64(): ByteArray {
    return Base64.decode(this?.toByteArray(), Base64.DEFAULT)
}
