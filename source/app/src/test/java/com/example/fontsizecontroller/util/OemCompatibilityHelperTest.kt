package com.example.fontsizecontroller.util

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OemCompatibilityHelperTest {

    @Test
    fun getFriendlyDeviceName_returnsNonEmptyString() {
        val name = OemCompatibilityHelper.getFriendlyDeviceName()
        assertNotNull(name)
        assertTrue(name.isNotBlank())
    }

    @Test
    fun manufacturerConstants_areConsistent() {
        // Assert that helper booleans match the manufacturer string safely
        val m = OemCompatibilityHelper.manufacturer
        if (m.contains("XIAOMI")) {
            assertTrue(OemCompatibilityHelper.isXiaomi)
        }
        if (m.contains("SAMSUNG")) {
            assertTrue(OemCompatibilityHelper.isSamsung)
        }
    }
}
