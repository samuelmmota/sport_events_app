package s.m.mota.sporteventsapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import s.m.mota.sporteventsapp.models.SportCategory
import s.m.mota.sporteventsapp.repository.SportsRepository
import s.m.mota.sporteventsapp.retrofit.SportEventsApi

class SportsViewModel : ViewModel() {
    companion object {
        val TAG: String = "SportsViewModel"
    }

    private val sportsRepository = SportsRepository(SportEventsApi)
    private val _sportCategories = MutableStateFlow<List<SportCategory>>(emptyList())
    val sportCategories: StateFlow<List<SportCategory>> = _sportCategories

    fun loadSportCategories() {
        viewModelScope.launch {
            try {
                val response = sportsRepository.fetchSportCategories()
                _sportCategories.value = response
                Log.i(TAG, "_sportCategories.size: ${_sportCategories.value.size.toString()}")
                for (sport in _sportCategories.value) {
                    Log.i(TAG, sport.toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading Sport Categories: $e")
            }
        }
    }
}