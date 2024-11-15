package s.m.mota.sporteventsapp.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import s.m.mota.sporteventsapp.models.SportCategory
import s.m.mota.sporteventsapp.retrofit.SportEventsApi

class SportsRepository(private val sportEventsApi: SportEventsApi) {
    private val TAG = "SportsRepository"

    suspend fun fetchSportCategories(): List<SportCategory> {
        return withContext(Dispatchers.IO) {
            try {
                val result = sportEventsApi.getSportCategories() ?: emptyList()
                result.filterNotNull()
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error. Fetch unsuccessful.", e)
                emptyList<SportCategory>()
            }
        }
    }
}