package s.m.mota.sporteventsapp.models

import com.google.gson.annotations.SerializedName

data class SportCategory(
    @SerializedName("i") val id: String?,
    @SerializedName("d") val name: String?,
    @SerializedName("e") val events: List<SportEvent>?
)