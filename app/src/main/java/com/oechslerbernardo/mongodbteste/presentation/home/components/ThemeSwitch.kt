package com.oechslerbernardo.mongodbteste.presentation.home.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemeSwitch(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {

    Log.d("ThemeSwitch", "Composing ThemeSwitch with isDarkTheme: $isDarkTheme")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDarkTheme) {
            Icon(imageVector = Icons.Outlined.NightsStay, contentDescription = null)
        } else {
            Icon(imageVector = Icons.Outlined.WbSunny, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Dark Mode")
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isDarkTheme,
            onCheckedChange = onToggleTheme,
            thumbContent = {
                if (isDarkTheme) {
                    Icon(imageVector = Icons.Filled.NightsStay, contentDescription = null)
                } else {
                    Icon(imageVector = Icons.Filled.WbSunny, contentDescription = null)
                }
            }
        )
    }
}