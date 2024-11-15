package s.m.mota.sporteventsapp.models

import com.google.gson.annotations.SerializedName

data class SportEvent(
    @SerializedName("i") val id: String?,
    @SerializedName("d") val name: String?,
    @SerializedName("tt") val startTime: Int?,
    @SerializedName("si") val sportId: String?
) {
    fun getCompetitors(): List<String> {
        val competitors = this.name?.split("-")?.map { it.trim() } ?: emptyList()
        return if (competitors.size == 2) {
            competitors
        } else {
            listOf("Unknown Competitor", "Unknown Competitor")
        }
    }
}