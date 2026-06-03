package com.example.prepdeck.data.local

import androidx.room.TypeConverter

class ColorListConverter {



    @TypeConverter
    fun fromColorList(colorsList: List<Int>): String {
        return colorsList.joinToString(",") { it.toString() }
    }


    @TypeConverter
    fun toColorList(colorsListString: String): List<Int> {
        return colorsListString.split(",").map { it.toInt() }

    }

}
