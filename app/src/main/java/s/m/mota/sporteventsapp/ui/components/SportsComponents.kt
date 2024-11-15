package s.m.mota.sporteventsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import s.m.mota.sporteventsapp.models.SportCategory
import s.m.mota.sporteventsapp.models.SportEvent

@Composable
fun SportCategoryItem(
    sportCategory: SportCategory,
    isCollapsed: Boolean,
    isFavorite: Boolean,
    favoriteEventIds: List<String>,
    onCollapseCategoryClick: () -> Unit,
    onFilterFavoriteCategoryEventsToggle: () -> Unit,
    onFavoriteEventClick: (SportEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onCollapseCategoryClick.invoke() }
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = sportCategory.name ?: "Unknown Category",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onFilterFavoriteCategoryEventsToggle) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                        tint = if (isFavorite) Color.Blue else Color.Gray
                    )
                }

                Icon(
                    imageVector = if (isCollapsed) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isCollapsed) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }
        }

        if (!isCollapsed) {
            sportCategory.events?.let { events ->
                events.forEach { event ->
                    SportEventItem(event = event,
                        isFavorite = favoriteEventIds.contains(event.id),
                        onFavoriteEventClick = { onFavoriteEventClick(event) })
                }
            }
        }
    }
}

@Composable
fun SportEventItem(event: SportEvent, isFavorite: Boolean, onFavoriteEventClick: () -> Unit) {
    var countdown by remember { mutableLongStateOf(getCountdownTime(event.startTime ?: 0)) }

    LaunchedEffect(event.startTime) {
        while (countdown > 0L) {
            delay(1000L)
            countdown = getCountdownTime(event.startTime ?: 0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(2.5.dp, Color.LightGray, RoundedCornerShape(2.5.dp))

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val competitors = event.getCompetitors()

                Text(
                    text = competitors[0],
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "VS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = competitors[1],
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )

                IconButton(
                    onClick = onFavoriteEventClick, modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = if (isFavorite) "Unfavorite this event" else "Favorite this event",
                        tint = if (isFavorite) Color.Yellow else Color.Gray
                    )
                }
            }

            Text(
                text = if (countdown > 0L) "Starts in: ${formatCountdown(countdown)}" else "Event Ended",
                fontSize = 14.sp,
                color = if (countdown > 0L) Color.Green else Color.Red,
                style = TextStyle(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}

fun getCountdownTime(startTime: Int): Long {
    val currentUnixTime = System.currentTimeMillis() / 1000
    return (startTime - currentUnixTime).coerceAtLeast(0L)
}

fun formatCountdown(unixTimestamp: Long): String {
    val days = unixTimestamp / (60 * 60 * 24)
    val hours = (unixTimestamp % (60 * 60 * 24)) / (60 * 60)
    val minutes = (unixTimestamp % (60 * 60)) / 60
    val seconds = unixTimestamp % 60
    return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
}


@Preview(showBackground = true)
@Composable
fun SportCategoryItemPreview() {
    val mockSportCategory = SportCategory(
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
    )
    SportCategoryItem(sportCategory = mockSportCategory,
        onFilterFavoriteCategoryEventsToggle = {},
        onCollapseCategoryClick = {},
        onFavoriteEventClick = {},
        isFavorite = false,
        favoriteEventIds = emptyList(),
        isCollapsed = false
    )
}

@Preview(showBackground = true)
@Composable
fun SportEventItemPreview() {

    val mockSportEvent = SportEvent(
        id = "1",
        name = "Benfica  -  Porto",
        startTime = 1738673988,
        sportId = "FOOT",
    )

    SportEventItem(event = mockSportEvent, isFavorite = true, onFavoriteEventClick = {})
}