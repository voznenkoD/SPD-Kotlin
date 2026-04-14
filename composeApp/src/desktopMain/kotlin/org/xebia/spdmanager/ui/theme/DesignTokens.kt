package org.xebia.spdmanager.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Colors ---

val ColorBackground = Color(0xFFF0F0F0)
val ColorSurface = Color(0xFFE4E4E4)
val ColorSurfaceSelected = Color(0xFF5A5A5A)
val ColorSurfaceHover = Color(0xFFD6D6D6)
val ColorDivider = Color(0xFFCCCCCC)

val ColorAccentOrange = Color(0xFFC45C00)
val ColorAccentYellow = Color(0xFFC49A00)

val ColorTextPrimary = Color(0xFF1A1A1A)
val ColorTextSecondary = Color(0xFF5A5A5A)
val ColorTextOnDark = Color(0xFFF5C542)
val ColorTextOnAccent = Color(0xFFFFFFFF)
val ColorTextDisabled = Color(0xFFAAAAAA)

// --- Shapes ---

val ShapeSmall = RoundedCornerShape(1.dp)
val ShapeDefault = RoundedCornerShape(2.dp)
val ShapeCard = RoundedCornerShape(4.dp)

// --- Base Typography (unscaled) ---

private const val TITLE_SIZE = 13f
private const val BODY_SIZE = 11f
private const val LABEL_SIZE = 10f
private const val CAPTION_SIZE = 9f
private const val MONO_SIZE = 10f

// --- Base Spacing (unscaled) ---

private const val SPACE_XS_VAL = 1f
private const val SPACE_S_VAL = 2f
private const val SPACE_M_VAL = 4f
private const val SPACE_L_VAL = 6f
private const val SPACE_XL_VAL = 8f
private const val SPACE_XXL_VAL = 16f

// --- Base Heights (unscaled) ---

private const val HEIGHT_CONTROL_VAL = 24f
private const val HEIGHT_BUTTON_VAL = 26f
private const val HEIGHT_LIST_ITEM_VAL = 28f
private const val HEIGHT_SECTION_BAR_VAL = 22f

// --- Scale Factor ---

val LocalScale = compositionLocalOf { 1f }

const val BASE_WIDTH = 1280f

// --- Scaled accessors ---

object Spacing {
    val xs: Dp @Composable get() = (SPACE_XS_VAL * LocalScale.current).dp
    val s: Dp @Composable get() = (SPACE_S_VAL * LocalScale.current).dp
    val m: Dp @Composable get() = (SPACE_M_VAL * LocalScale.current).dp
    val l: Dp @Composable get() = (SPACE_L_VAL * LocalScale.current).dp
    val xl: Dp @Composable get() = (SPACE_XL_VAL * LocalScale.current).dp
    val xxl: Dp @Composable get() = (SPACE_XXL_VAL * LocalScale.current).dp
}

object Heights {
    val control: Dp @Composable get() = (HEIGHT_CONTROL_VAL * LocalScale.current).dp
    val button: Dp @Composable get() = (HEIGHT_BUTTON_VAL * LocalScale.current).dp
    val listItem: Dp @Composable get() = (HEIGHT_LIST_ITEM_VAL * LocalScale.current).dp
    val sectionBar: Dp @Composable get() = (HEIGHT_SECTION_BAR_VAL * LocalScale.current).dp
}

object Typography {
    val title: TextStyle @Composable get() = TextStyle(
        fontSize = (TITLE_SIZE * LocalScale.current).sp,
        fontWeight = FontWeight.SemiBold
    )
    val body: TextStyle @Composable get() = TextStyle(
        fontSize = (BODY_SIZE * LocalScale.current).sp,
        fontWeight = FontWeight.Normal
    )
    val label: TextStyle @Composable get() = TextStyle(
        fontSize = (LABEL_SIZE * LocalScale.current).sp,
        fontWeight = FontWeight.Normal
    )
    val caption: TextStyle @Composable get() = TextStyle(
        fontSize = (CAPTION_SIZE * LocalScale.current).sp,
        fontWeight = FontWeight.Normal
    )
    val mono: TextStyle @Composable get() = TextStyle(
        fontSize = (MONO_SIZE * LocalScale.current).sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.Monospace
    )

    val titleSize: TextUnit @Composable get() = (TITLE_SIZE * LocalScale.current).sp
    val bodySize: TextUnit @Composable get() = (BODY_SIZE * LocalScale.current).sp
    val labelSize: TextUnit @Composable get() = (LABEL_SIZE * LocalScale.current).sp
    val captionSize: TextUnit @Composable get() = (CAPTION_SIZE * LocalScale.current).sp
    val monoSize: TextUnit @Composable get() = (MONO_SIZE * LocalScale.current).sp
}
