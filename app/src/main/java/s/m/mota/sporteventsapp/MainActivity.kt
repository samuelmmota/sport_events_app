package s.m.mota.sporteventsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import s.m.mota.sporteventsapp.ui.screens.MainScreen
import s.m.mota.sporteventsapp.ui.theme.SportEventsAppTheme
import s.m.mota.sporteventsapp.viewmodel.SportsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sportsViewModel = SportsViewModel()
        sportsViewModel.loadSportCategories()

        enableEdgeToEdge()
        setContent {
            SportEventsAppTheme {
                MainScreen(sportsViewModel = sportsViewModel,
                    onFilterFavoritesToggle = { sportCategory ->
                        sportCategory.id?.let { sportsViewModel.updateFavoriteSportCategory(it) }
                    },
                    onCollapseCategoryClick = { sportCategory ->
                        sportCategory.id?.let { sportsViewModel.updateCollapsedSportCategory(it) }
                    },
                    onFavoriteEventClick = { sportEvent ->
                        sportEvent.id?.let { sportsViewModel.updateFavoriteSportEvent(it) }
                    })
            }
        }
    }
}