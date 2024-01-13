package com.oechslerbernardo.mongodbteste.presentation.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oechslerbernardo.mongodbteste.R
import com.oechslerbernardo.mongodbteste.presentation.main.MainEvent

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    isDarkTheme: Boolean,
    onEvent: (MainEvent) -> Unit,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(modifier = Modifier.requiredWidth(320.dp), content = {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Image"
                )
                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Sign out"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Sign Out", color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }, selected = false, onClick = { onEvent(MainEvent.OnDialogOpen) }
                )
                NavigationDrawerItem(
                    label = {
                        Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete All Icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Delete All Diaries",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }, selected = false, onClick = { }
                )
                NavigationDrawerItem(
                    label = {
                        ThemeSwitch(isDarkTheme = isDarkTheme, onToggleTheme = { isChecked ->
                            onEvent(MainEvent.ToggleThemeSwitch(isChecked))
                        })
                    }, selected = false, onClick = { }
                )
            })
        }, content = content
    )
}