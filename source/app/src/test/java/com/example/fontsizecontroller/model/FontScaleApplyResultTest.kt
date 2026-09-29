package com.example.fontsizecontroller.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FontScaleApplyResultTest {

    @Test
    fun securityBlocked_containsDetails() {
        val result = FontScaleApplyResult.SecurityBlocked(
            message = "Blocked by Device Policy Manager",
            isMdmRestricted = true
        )

        assertEquals("Blocked by Device Policy Manager", result.message)
        assertTrue(result.isMdmRestricted)
    }

    @Test
    fun applyUiResult_securityBlocked_matches() {
        val uiResult: ApplyUiResult = ApplyUiResult.SecurityBlocked(
            message = "MDM lock",
            isMdmRestricted = true
        )

        assertTrue(uiResult is ApplyUiResult.SecurityBlocked)
        assertEquals("MDM lock", (uiResult as ApplyUiResult.SecurityBlocked).message)
        assertTrue(uiResult.isMdmRestricted)
    }
}
