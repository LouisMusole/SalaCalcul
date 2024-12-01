package com.drcmind.salacalcul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.drcmind.salacalcul.ui.theme.SalaCalculTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SalaCalculTheme {

                val navigator = rememberSupportingPaneScaffoldNavigator()
                val scope = rememberCoroutineScope()

                val history = remember { mutableStateListOf<String>() }

                BackHandler(navigator.canNavigateBack()) {
                    scope.launch{
                        navigator.navigateBack()
                    }
                }

                SupportingPaneScaffold(
                    directive = navigator.scaffoldDirective,
                    value = navigator.scaffoldValue,
                    mainPane = {
                        AnimatedPane{
                            MainPaneScreen(
                                onNavigateToSupportingPane = {
                                    scope.launch{
                                        navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                    }
                                },
                                onAddToHistory = {
                                    history.add(it)
                                },
                                isSupportingPaneHidden = navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden
                            )
                        }
                    },
                    supportingPane = {
                        AnimatedPane {
                            SupportPaneScreen(
                                history = history,
                                isMainPanelHidden = navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden,
                                onDeleteAllStory = {
                                    history.clear()
                                },
                                onNavigateBack = {
                                    scope.launch{
                                        navigator.navigateBack()
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

