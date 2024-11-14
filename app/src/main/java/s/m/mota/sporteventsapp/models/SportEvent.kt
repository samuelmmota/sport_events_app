package s.m.mota.sporteventsapp.models

import com.google.gson.annotations.SerializedName

data class SportEvent(
    @SerializedName("i") val id: String,
    @SerializedName("d") val name: String,
    @SerializedName("tt") val startTime: Long,
    @SerializedName("si") val sportId: String
    //val sh: String, // same as d
)
