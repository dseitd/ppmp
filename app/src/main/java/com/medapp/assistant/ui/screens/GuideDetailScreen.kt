package com.medapp.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medapp.assistant.R
import com.medapp.assistant.ui.viewmodels.GuideDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideDetailScreen(
    navController: NavController,
    guideId: Long,
    viewModel: GuideDetailViewModel = hiltViewModel()
) {
    val guide by viewModel.guide.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()
    val totalSteps by viewModel.totalSteps.collectAsState()

    LaunchedEffect(guideId) {
        viewModel.loadGuide(guideId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(guide?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.previous)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { guide?.let { viewModel.saveGuideOffline(it) } }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = stringResource(R.string.save_offline)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Описание руководства
            guide?.let { currentGuide ->
                Text(
                    text = currentGuide.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Шаги
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Навигация по шагам
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { viewModel.previousStep() },
                                enabled = currentStep > 0
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                                    contentDescription = stringResource(R.string.previous)
                                )
                                Text(stringResource(R.string.previous))
                            }

                            Text(
                                text = stringResource(
                                    R.string.step_of_total,
                                    currentStep + 1,
                                    totalSteps
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )

                            TextButton(
                                onClick = { viewModel.nextStep() },
                                enabled = currentStep < totalSteps - 1
                            ) {
                                Text(stringResource(R.string.next))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                    contentDescription = stringResource(R.string.next)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Содержимое текущего шага
                        currentGuide.steps.getOrNull(currentStep)?.let { step ->
                            Column {
                                Text(
                                    text = step.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                step.warning?.let { warning ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = warning,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                
                                step.note?.let { note ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = note,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 