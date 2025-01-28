package com.alielahi.securedatastore

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alielahi.securedatastore.storeData.SecureDataStoreManager

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.alielahi.securedatastore", appContext.packageName)
    }


    @Test
    fun testStringSecureDataStore(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val dataStoreManager = SecureDataStoreManager.getSecureDataStore(appContext)

        dataStoreManager.setString(key = "test" , value = "data from test")

        val retrieveData = dataStoreManager.getString(key = "test")

        assertEquals("data from test" , retrieveData)


        dataStoreManager.setString(key = "test" , value = "hello")

        assertEquals("hello" , dataStoreManager.getString(key = "test"))
    }
}