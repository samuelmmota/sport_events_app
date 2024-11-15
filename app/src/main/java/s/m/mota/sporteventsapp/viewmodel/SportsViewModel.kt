package s.m.mota.sporteventsapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState
    private val _collapsedCategoryIds = MutableStateFlow<List<String>>(emptyList())
    val collapsedCategoryIds: StateFlow<List<String>> = _collapsedCategoryIds
    private val _favoriteCategoryIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteCategoryIds: StateFlow<List<String>> = _favoriteCategoryIds
    private val _favoriteEventIds = MutableStateFlow<List<String>>(emptyList())
    val favoriteEventIds: StateFlow<List<String>> = _favoriteEventIds

    val filteredSportCategoriesAndEvents: StateFlow<List<SportCategory>> = combine(
        _sportCategories, _favoriteCategoryIds, _favoriteEventIds
    ) { sports, favoriteCategories, favoriteEvents ->
        if (favoriteCategories.isEmpty()) {
            sports
        } else {
            sports.map { category ->
                val isFavoriteCategory = favoriteCategories.any { it == category.id }

                val filteredEvents = if (isFavoriteCategory) {
                    category.events?.filter { event ->
                        favoriteEvents.any { it == event.id }
                    }
                } else {
                    category.events
                }

                category.copy(events = filteredEvents)
            }
        }
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.Lazily, initialValue = emptyList()
    )

    fun loadSportCategories() {
        viewModelScope.launch {
            try {
                val response = sportsRepository.fetchSportCategories()
                _sportCategories.value = response
                _errorState.value = null
                Log.i(TAG, "_sportCategories.size: ${_sportCategories.value.size.toString()}")
                for (sport in _sportCategories.value) {
                    Log.i(TAG, sport.toString())
                }
            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Unknown error"
                Log.e(TAG, "Error loading Sport Categories: $e")
            }
        }
    }

    fun updateCollapsedSportCategory(sportCategoryId: String) {
        if (_sportCategories.value.any { it.id == sportCategoryId }) {
            if (!_collapsedCategoryIds.value.any { it == sportCategoryId }) {
                _collapsedCategoryIds.value += sportCategoryId
            } else {
                _collapsedCategoryIds.value -= sportCategoryId
            }
        }
    }

    fun updateFavoriteSportCategory(sportCategoryId: String) {
        if (_sportCategories.value.any { it.id == sportCategoryId }) {
            if (!_favoriteCategoryIds.value.any { it == sportCategoryId }) {
                _favoriteCategoryIds.value += sportCategoryId
            } else {
                _favoriteCategoryIds.value -= sportCategoryId
            }
        }
    }

    fun updateFavoriteSportEvent(sportEventId: String) {
        if (_sportCategories.value.any { sportCategory ->
                sportCategory.events?.any { it.id == sportEventId } ?: false
            }) {
            if (!_favoriteEventIds.value.any { it == sportEventId }) {
                _favoriteEventIds.value += sportEventId
            } else {
                _favoriteEventIds.value -= sportEventId
            }
        }
    }
}