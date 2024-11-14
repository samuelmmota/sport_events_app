package s.m.mota.sporteventsapp.models

import com.google.gson.annotations.SerializedName

data class SportsApiResponse(
    @SerializedName("sports") val sports: List<SportsCategory>,
)