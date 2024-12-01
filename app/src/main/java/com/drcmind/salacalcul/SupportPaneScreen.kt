package com.drcmind.salacalcul

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportPaneScreen(
    history : List<String>,
    isMainPanelHidden : Boolean,
    onDeleteAllStory : ()->Unit,
    onNavigateBack : ()->Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (isMainPanelHidden){
                        IconButton(
                            onClick = {
                                onNavigateBack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                title = {
                    Text("Historique")
                },
                actions = {
                    IconButton(
                        onClick = {onDeleteAllStory()}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.clear_all),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValue->
        Box(
            modifier = Modifier.padding(paddingValue).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if(history.isEmpty()){
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.hot_tub),
                        contentDescription = null,
                        modifier = Modifier.size(58.dp),
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                    Text("Vide")
                }

            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(history){
                        ListItem(
                            headlineContent = { Text(it) }
                        )
                    }
                }
            }
        }
    }
}