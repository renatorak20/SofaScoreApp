package com.example.sofascoreapp.data.model

import com.google.gson.*
import java.lang.reflect.Type

class ScoreConverter : JsonDeserializer<Score>, JsonSerializer<Score> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Score {
        return when {
            json.isJsonObject -> context.deserialize(json, Score::class.java)
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




