package com.timurkhabibulin.myhabits.db

import android.graphics.Color
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun toColor(colorValue: Int): Color = Color.valueOf(colorValue)

    @TypeConverter
    fun fromColor(color: Color): Int = color.toArgb()
}