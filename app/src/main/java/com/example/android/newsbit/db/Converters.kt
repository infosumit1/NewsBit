package com.example.android.newsbit.db

import androidx.room.TypeConverter
import com.example.android.newsbit.models.Source

class Converters {

    /* Note: Sometimes, you need your app to store a custom data type in a single database column.
    You support custom types by providing type converters, which are methods that tell Room how to
    convert custom types to and from known types that Room can persist. */

    @TypeConverter
    fun fromSource(source: Source): String {
        //save only source name. I will not display source id because it is unnecessary
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        //pass source name as id when converting back to Source(news source) type
        return Source(name, name)
    }
}