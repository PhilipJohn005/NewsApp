package com.example.newsclientapp.data.db

import androidx.room.TypeConverter
import com.example.newsclientapp.data.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source?): String {
        return source?.name?:"Unknown"
    }

    @TypeConverter
    fun toSource(name: String?): Source {
        return Source(id=name?:"unknown",name =name?:"Unknown")
    }
}
