package io.alexeychurchill.dplayer.ui.theme

import androidx.compose.material3.Typography
import io.alexeychurchill.dplayer.ui.fonts.urbanistFontFamily

private val Default = Typography()

val Typography = Typography(

    displayLarge = Default.displayLarge.copy(fontFamily = urbanistFontFamily),
    displayMedium = Default.displayMedium.copy(fontFamily = urbanistFontFamily),
    displaySmall = Default.displaySmall.copy(fontFamily = urbanistFontFamily),

    headlineLarge = Default.headlineLarge.copy(fontFamily = urbanistFontFamily),
    headlineMedium = Default.headlineMedium.copy(fontFamily = urbanistFontFamily),
    headlineSmall = Default.headlineSmall.copy(fontFamily = urbanistFontFamily),

    titleLarge = Default.titleLarge.copy(fontFamily = urbanistFontFamily),
    titleMedium = Default.titleMedium.copy(fontFamily = urbanistFontFamily),
    titleSmall = Default.titleSmall.copy(fontFamily = urbanistFontFamily),

    bodyLarge = Default.bodyLarge.copy(fontFamily = urbanistFontFamily),
    bodyMedium = Default.bodyMedium.copy(fontFamily = urbanistFontFamily),
    bodySmall = Default.bodySmall.copy(fontFamily = urbanistFontFamily),

    labelLarge = Default.labelLarge.copy(fontFamily = urbanistFontFamily),
    labelMedium = Default.labelMedium.copy(fontFamily = urbanistFontFamily),
    labelSmall = Default.labelSmall.copy(fontFamily = urbanistFontFamily),
)
