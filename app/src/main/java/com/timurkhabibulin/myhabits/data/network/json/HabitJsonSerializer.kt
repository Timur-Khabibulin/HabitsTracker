package com.timurkhabibulin.myhabits.data.network.json

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.timurkhabibulin.myhabits.data.network.HabitNetworkEntity
import java.lang.reflect.Type

class HabitJsonSerializer : JsonSerializer<HabitNetworkEntity> {

    override fun serialize(
        src: HabitNetworkEntity,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject().apply {
        addProperty("color", src.color)
        addProperty("count", src.count)
        addProperty("date", src.date)
        addProperty("description", src.description)
//        add("done_dates", JsonArray().apply { src.doneDates.forEach { add(it) } })
        addProperty("frequency", src.frequency)
        addProperty("priority", src.priority)
        addProperty("title", src.title)
        addProperty("type", src.type)
        addProperty("uid", src.uid)
    }
}