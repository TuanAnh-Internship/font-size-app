package com.example.fontsizecontroller.model

/**
 * Định nghĩa Hồ sơ cỡ chữ gia đình (Family Profiles).
 * Giúp người dùng 1-chạm đổi nhanh cỡ chữ khi chuyển giao điện thoại cho các thành viên trong gia đình.
 */
data class FamilyProfile(
    val id: String,
    val nameVi: String,
    val nameEn: String,
    val scale: Float,
    val descriptionVi: String,
    val descriptionEn: String
) {
    companion object {
        val defaultProfiles = listOf(
            FamilyProfile(
                id = "myself",
                nameVi = "Cá Nhân",
                nameEn = "Myself",
                scale = 1.00f,
                descriptionVi = "Chuẩn mặc định 1.00x - Đọc nhanh, sắc nét",
                descriptionEn = "Standard 1.00x - Fast & crisp reading"
            ),
            FamilyProfile(
                id = "parents",
                nameVi = "Bố Mẹ",
                nameEn = "Parents",
                scale = 1.15f,
                descriptionVi = "Cỡ chữ lớn 1.15x - Giảm mỏi mắt, rõ ràng",
                descriptionEn = "Large 1.15x - Less eye strain, clearer"
            ),
            FamilyProfile(
                id = "grandparents",
                nameVi = "Ông Bà",
                nameEn = "Grandparents",
                scale = 1.30f,
                descriptionVi = "Cỡ rất lớn 1.30x - Dễ đọc cho mắt yếu, lão thị",
                descriptionEn = "Extra large 1.30x - Easy reading for seniors"
            )
        )
    }
}
