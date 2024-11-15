package s.m.mota.sporteventsapp.retrofit

import android.util.Log
import com.google.gson.*
import s.m.mota.sporteventsapp.models.SportCategory
import s.m.mota.sporteventsapp.models.SportEvent
import java.lang.reflect.Type

import com.google.gson.JsonObject

class SportCategoryDeserializer : JsonDeserializer<SportCategory?> {
    private val TAG: String = "SportCategoryDeserializer"
    override fun deserialize(
        json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): SportCategory? {

        val jsonObject = json.asJsonObject

        return try {
            parseSportCategory(jsonObject)
        } catch (e: Exception) {
            Log.e(TAG, "Error deserializing jsonObject into SportCategory")
            null
        }
    }


    private fun parseSportCategory(jsonObject: JsonObject): SportCategory? {
        return try {
            val idElement = jsonObject.get("i")
            val id =
                if (idElement != null && idElement.isJsonPrimitive && idElement.asJsonPrimitive.isString) {
                    idElement.asString
                } else {
                    return null
                }

            val nameElement = jsonObject.get("d")
            val name =
                if (nameElement != null && nameElement.isJsonPrimitive && nameElement.asJsonPrimitive.isString) {
                    nameElement.asString
                } else {
                    null
                }

            val eventsElement = jsonObject.get("e")
            val events = if (eventsElement != null && eventsElement.isJsonArray) {
                val eventList = mutableListOf<SportEvent>()
                for (eventJson in eventsElement.asJsonArray) {
                    if (eventJson.isJsonObject) {
                        val event = parseSportEvent(eventJson.asJsonObject)
                        if (event != null) {
                            eventList.add(event)
                        }
                    }
                }
                eventList
            } else {
                null
            }

            SportCategory(id = id, name = name, events = events)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing SportCategory: ${e.message}")
            null
        }
    }

    private fun parseSportEvent(eventJsonObject: JsonObject): SportEvent? {
        return try {
            val eventIdElement = eventJsonObject.get("i")
            val eventId =
                if (eventIdElement != null && eventIdElement.isJsonPrimitive && eventIdElement.asJsonPrimitive.isString) {
                    eventIdElement.asString
                } else {
                    return null
                }

            val eventNameElement = eventJsonObject.get("d")
            val eventName =
                if (eventNameElement != null && eventNameElement.isJsonPrimitive && eventNameElement.asJsonPrimitive.isString) {
                    eventNameElement.asString
                } else {
                    null
                }

            val startingTimeElement = eventJsonObject.get("tt")
            val startingTime =
                if (startingTimeElement != null && startingTimeElement.isJsonPrimitive && startingTimeElement.asJsonPrimitive.isNumber) {
                    startingTimeElement.asInt
                } else {
                    null
                }

            val sportIdElement = eventJsonObject.get("si")
            val sportId =
                if (sportIdElement != null && sportIdElement.isJsonPrimitive && sportIdElement.asJsonPrimitive.isString) {
                    sportIdElement.asString
                } else {
                    null
                }

            SportEvent(
                id = eventId, name = eventName, startTime = startingTime, sportId = sportId
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing SportEvent: ${e.message}")
            null
        }
    }
}