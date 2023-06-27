package com.timurkhabibulin.data.network.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.timurkhabibulin.data.network.HabitNetworkEntity
import java.lang.reflect.Type

class HabitJsonDeserializer : JsonDeserializer<HabitNetworkEntity> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HabitNetworkEntity = HabitNetworkEntity(
        json.asJsonObject.get("uid").asString,
        json.asJsonObject.get("date").asInt,
        json.asJsonObject.get("count").asInt,
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        json.asJsonObject.get("frequency").asInt,
        json.asJsonObject.get("priority").asInt,
        json.asJsonObject.get("type").asInt,
        json.asJsonObject.get("color").asInt
    )

}