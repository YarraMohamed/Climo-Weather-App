package com.example.climo.view.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.climo.R

val OutfitBold = FontFamily(
    Font(R.font.outfit_bold)
)

val InterExtraBold = FontFamily(
    Font(R.font.inter_extra_bold)
)

val InterBold = FontFamily(
    Font(R.font.inter_bold)
)

val RobotoRegular = FontFamily(
    Font(R.font.roboto_regular)
)

val RobotoMedium = FontFamily(
    Font(R.font.roboto_medium)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)


