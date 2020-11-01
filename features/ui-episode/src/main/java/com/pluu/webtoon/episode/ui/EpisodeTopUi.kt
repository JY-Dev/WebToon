package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.ui.tooling.preview.Preview
import com.pluu.webtoon.ui.compose.graphics.toColor

@Composable
fun EpisodeTopUi(
    title: String,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onFavoriteClicked: (isFavorite: Boolean) -> Unit
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        title = {
            Text(text = title, color = Color.White)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Filled.ArrowBack, tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {
                onFavoriteClicked(isFavorite)
            }) {
                if (isFavorite) {
                    Icon(
                        asset = Icons.Default.Favorite,
                        tint = 0xFFF44336.toColor()
                    )
                } else {
                    Icon(
                        asset = Icons.Default.FavoriteBorder,
                        tint = Color.White
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun previewEpisodeTopUi() {
    EpisodeTopUi(
        title = "테스트",
        isFavorite = true,
        onFavoriteClicked = {},
        onBackPressed = {}
    )
}