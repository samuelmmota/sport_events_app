package s.m.mota.sporteventsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import s.m.mota.sporteventsapp.models.SportCategory
import s.m.mota.sporteventsapp.models.SportEvent
import s.m.mota.sporteventsapp.ui.components.SportCategoryItem
import s.m.mota.sporteventsapp.viewmodel.SportsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    sportsViewModel: SportsViewModel,
    onFilterFavoritesToggle: (SportCategory) -> Unit,
    onFavoriteEventClick: (SportEvent) -> Unit,
    onCollapseCategoryClick: (SportCategory) -> Unit,
) {
    val sports by sportsViewModel.filteredSportCategoriesAndEvents.collectAsState(emptyList())
    val favoriteCategoryIds by sportsViewModel.favoriteCategoryIds.collectAsState(emptyList())
    val favoriteEventIds by sportsViewModel.favoriteEventIds.collectAsState(emptyList())
    val collapsedCategoryIds by sportsViewModel.collapsedCategoryIds.collectAsState(emptyList())
    val errorState by sportsViewModel.errorState.collectAsState(null)
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Sport Events App", color = Color.Black)
        })
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        when {
            errorState != null -> {
                NoSportCategoriesMessage(
                    "Error Loading Sport Events: $errorState", innerPadding, Color.Red
                )
            }

            sports.isEmpty() -> {
                NoSportCategoriesMessage("No Sport Events Available", innerPadding, Color.Gray)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), contentPadding = innerPadding
                ) {
                    items(sports) { category ->
                        SportCategoryItem(
                            category,
                            isFavorite = favoriteCategoryIds.contains(category.id),
                            isCollapsed = collapsedCategoryIds.contains(category.id),
                            onCollapseCategoryClick = { onCollapseCategoryClick(category) },
                            onFilterFavoriteCategoryEventsToggle = {
                                onFilterFavoritesToggle(
                                    category
                                )
                            },
                            favoriteEventIds = favoriteEventIds,
                            onFavoriteEventClick = onFavoriteEventClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoSportCategoriesMessage(text: String, innerPadding: PaddingValues, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, color = color, fontSize = 18.sp, modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val mockSportCategoryList = listOf(
        SportCategory(
            id = "1",
            name = "SOCCER",
            events = listOf(
                SportEvent(
                    id = "1",
                    name = "Benfica - Porto",
                    startTime = 1010101,
                    sportId = "FOOT",
                ), SportEvent(
                    id = "2",
                    name = "Benfica - Porto",
                    startTime = 1010101,
                    sportId = "FOOT",
                )
            ),
        ),
        SportCategory(
            id = "2",
            name = "BASKET",
            events = listOf(
                SportEvent(
                    id = "1",
                    name = "Benfica - Sporting CP",
                    startTime = 1010101,
                    sportId = "HAND",
                ), SportEvent(
                    id = "2",
                    name = "Benfica - Sporting CP",
                    startTime = 1010101,
                    sportId = "HAND",
                )
            ),
        ),
    )

    /* val mockViewModel = object : SportsViewModel() {
         override val sportCategories = MutableStateFlow(mockSportCategoryList)

     }
     MainScreen(sportsViewModel = mockViewModel,
         onFilterFavoritesToggle = {},
         onFavoriteEventClick = { })

     */
}