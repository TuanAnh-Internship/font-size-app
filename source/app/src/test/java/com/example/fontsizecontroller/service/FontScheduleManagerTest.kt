package com.example.fontsizecontroller.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

/**
 * Unit Test kiểm tra logic tính toán lịch hẹn thời gian (FontScheduleManager).
 */
class FontScheduleManagerTest {

    @Test
    fun calculateNextTriggerMillis_beforeTargetTime_schedulesOnSameDay() {
        // Giả sử bây giờ là 15:00 ngày 29/09/2026
        val now = Calendar.getInstance().apply {
            set(2026, Calendar.SEPTEMBER, 29, 15, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Hẹn lúc 20:00 tối (cùng ngày)
        val triggerMillis = FontScheduleManager.calculateNextTriggerMillis(
            hour = 20,
            minute = 0,
            nowMillis = now.timeInMillis
        )

        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMillis }

        assertEquals(2026, triggerCal.get(Calendar.YEAR))
        assertEquals(Calendar.SEPTEMBER, triggerCal.get(Calendar.MONTH))
        assertEquals(29, triggerCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(20, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, triggerCal.get(Calendar.MINUTE))
        assertTrue(triggerMillis > now.timeInMillis)
    }

    @Test
    fun calculateNextTriggerMillis_afterTargetTime_schedulesOnNextDay() {
        // Giả sử bây giờ là 21:30 tối ngày 29/09/2026
        val now = Calendar.getInstance().apply {
            set(2026, Calendar.SEPTEMBER, 29, 21, 30, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Hẹn lúc 20:00 tối (đã qua trong hôm nay -> phải sang ngày 30/09)
        val triggerMillis = FontScheduleManager.calculateNextTriggerMillis(
            hour = 20,
            minute = 0,
            nowMillis = now.timeInMillis
        )

        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMillis }

        assertEquals(2026, triggerCal.get(Calendar.YEAR))
        assertEquals(Calendar.SEPTEMBER, triggerCal.get(Calendar.MONTH))
        assertEquals(30, triggerCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(20, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, triggerCal.get(Calendar.MINUTE))
        assertTrue(triggerMillis > now.timeInMillis)
    }

    @Test
    fun calculateNextTriggerMillis_morningRestore_fromNightTime() {
        // Giả sử lúc 22:00 đêm hẹn khôi phục lúc 07:00 sáng mai
        val now = Calendar.getInstance().apply {
            set(2026, Calendar.SEPTEMBER, 29, 22, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val triggerMillis = FontScheduleManager.calculateNextTriggerMillis(
            hour = 7,
            minute = 0,
            nowMillis = now.timeInMillis
        )

        val triggerCal = Calendar.getInstance().apply { timeInMillis = triggerMillis }

        assertEquals(30, triggerCal.get(Calendar.DAY_OF_MONTH))
        assertEquals(7, triggerCal.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, triggerCal.get(Calendar.MINUTE))
    }
}
