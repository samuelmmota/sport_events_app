package s.m.mota.sporteventsapp.models

import com.google.gson.annotations.SerializedName

data class SportsCategory(
    @SerializedName("i") val id: String, //sport id
    @SerializedName("d") val name: String, //sport name
    @SerializedName("e") val events: List<SportEvent> //active events
)