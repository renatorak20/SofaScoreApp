package com.example.sofascoreapp.utils

import com.example.sofascoreapp.data.model.Score
import com.google.gson.*
import java.lang.reflect.Type

class ScoreConverter : JsonDeserializer<Score>, JsonSerializer<Score> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Score {
        return when {
            json.isJsonObject -> {
                val jsonObject = json.asJsonObject
                Score(
                    jsonObject.get("total")?.asInt,
                    jsonObject.get("period1")?.asInt,
                    jsonObject.get("period2")?.asInt,
                    jsonObject.get("period3")?.asInt,
                    jsonObject.get("period4")?.asInt,
                    jsonObject.get("period5")?.asInt
                )
            }

            else -> Score(null, null, null, null, null, null)
        }
    }

    override fun serialize(
        src: Score?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return if (src == null || src.isEmpty()) {
            JsonArray()
        } else {
            context.serialize(src)
        }
    }

    private fun Score.isEmpty(): Boolean {
        return total == null &&
                period1 == null &&
                period2 == null &&
                period3 == null &&
                period4 == null &&
                overtime == null
    }
}




