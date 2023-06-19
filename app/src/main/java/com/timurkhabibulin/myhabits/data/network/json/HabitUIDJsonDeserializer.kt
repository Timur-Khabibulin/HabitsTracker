package com.timurkhabibulin.myhabits.data.network.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitUIDJsonDeserializer  : JsonDeserializer<String> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String = json.asJsonObject.get("uid").asString

}