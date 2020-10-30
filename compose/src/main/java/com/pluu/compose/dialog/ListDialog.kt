package com.pluu.compose.dialog

import androidx.compose.foundation.ProvideTextStyle
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.AmbientEmphasisLevels
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideEmphasis
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.ui.tooling.preview.Preview

@Composable
fun <T> ListDialog(
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    items: List<T>,
    onDismiss: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = properties) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            dialogContent(
                title = title,
                buttons = buttons,
                items = items,
                itemContent = itemContent
            )
        }
    }
}

@Composable
private fun <T> dialogContent(
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    items: List<T>,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    val emphasisLevels = AmbientEmphasisLevels.current
    Column(modifier = modifier) {
        if (title != null) {
            Box(modifier = TitlePadding.align(Alignment.Start)) {
                ProvideEmphasis(emphasisLevels.high) {
                    val textStyle = MaterialTheme.typography.subtitle1
                    ProvideTextStyle(textStyle, title)
                }
            }
        }
        Divider()
        LazyColumnForIndexed(
            items = items,
            modifier = Modifier.fillMaxWidth(),
            itemContent = itemContent,
        )
        if (buttons != null) {
            Spacer(Modifier.preferredHeight(2.dp))
            buttons.invoke()
        }
        Spacer(Modifier.preferredHeight(1.dp))
    }
}

internal val TitlePadding = Modifier.padding(24.dp)
internal fun Modifier.ItemPadding() = padding(24.dp)

@Preview
@Composable
fun previewListDialog() {
    val list = (1..3).map {
        "Content $it"
    }
    Surface(color = Color.White) {
        dialogContent(
            title = {
                Text(text = "Test")
            },
            buttons = {
                Button(onClick = {}) {
                    Text(text = "Button")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            items = list
        ) { index, item ->
            Text(
                text = "$index : $item",
                modifier = Modifier.sizeIn(minHeight = 48.dp).wrapContentHeight()
            )
            Divider(color = Color.LightGray)
        }
    }
}
