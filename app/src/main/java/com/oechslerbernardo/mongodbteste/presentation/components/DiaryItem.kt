package com.oechslerbernardo.mongodbteste.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.mongodbteste.data.Diary
import com.oechslerbernardo.mongodbteste.util.toInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DiaryItem(diary: Diary, onClick: (String) -> Unit) {

    val formatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
    var isExpanded by remember { mutableStateOf(false) }

    BoxWithConstraints {
        val approximateCharsPerLine = maxWidth.value / 8
        val maxChars = approximateCharsPerLine * 4 // four lines
        val shouldShowButton = diary.description.length > maxChars

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {
                    Log.d("TAGY", "Diary clicked: ${diary._id}")
                    onClick(diary._id.toString())
                },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Text(
                modifier = Modifier.padding(14.dp),
                text = diary.title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                text = diary.description,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis
            )
            if (shouldShowButton) {
                if (!isExpanded) {
                    TextButton(
                        onClick = { isExpanded = true },
                    ) {
                        Text(text = AnnotatedString("Show more"))
                    }
                } else {
                    TextButton(
                        onClick = { isExpanded = false },
                    ) {
                        Text(text = AnnotatedString("Show less"))
                    }
                }
            }
            Spacer(modifier = Modifier.height(if (shouldShowButton) 0.dp else 7.dp))
            Text(
                modifier = Modifier.padding(start = 14.dp),
                text = formatter.format(diary.date.toInstant()),
                style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
            )
            Spacer(modifier = Modifier.height(7.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiaryItemPreview() {
    DiaryItem(diary = Diary().apply {
        title = "Teste"
        description =
            "Eu gosto, de te possuir, todinha pra mim, todinha pra mim, voce eh o amor da minha vida, eu te amo mais que tudo nesse mundo inteiro minha princesa linda e maravilhosa"
    },
        onClick = {}
    )
}