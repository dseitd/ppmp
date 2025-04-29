package com.medapp.assistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medapp.assistant.ui.viewmodels.QuizDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizDetailScreen(
    navController: NavController,
    quizId: Long,
    viewModel: QuizDetailViewModel = hiltViewModel()
) {
    val quiz by viewModel.quiz.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAnswers by viewModel.selectedAnswers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()

    var showExplanation by remember { mutableStateOf(false) }
    LaunchedEffect(currentQuestionIndex) { showExplanation = false }

    LaunchedEffect(quizId) {
        viewModel.loadQuiz(quizId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(quiz?.title ?: "Quiz") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (isCompleted) {
            QuizResultScreen(
                correctAnswers = viewModel.getCorrectAnswersCount(),
                totalQuestions = quiz?.questions?.size ?: 0,
                onFinish = { navController.popBackStack() }
            )
        } else {
            quiz?.let { currentQuiz ->
                val currentQuestion = currentQuiz.questions.getOrNull(currentQuestionIndex)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    LinearProgressIndicator(
                        progress = (currentQuestionIndex + 1).toFloat() / currentQuiz.questions.size,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    )

                    Text(
                        text = "Вопрос ${currentQuestionIndex + 1} из ${currentQuiz.questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    currentQuestion?.let { question ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = question.text,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )
                                question.options.forEachIndexed { index, option ->
                                    val isSelected = selectedAnswers[currentQuestionIndex] == index
                                    val isCorrect = question.correctAnswer == index
                                    val showAsCorrect = showExplanation && isCorrect
                                    val showAsIncorrect = showExplanation && isSelected && !isCorrect
                                    ElevatedButton(
                                        onClick = {
                                            if (!showExplanation) {
                                                viewModel.selectAnswer(currentQuestionIndex, index)
                                                showExplanation = true
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        enabled = !showExplanation,
                                        colors = ButtonDefaults.elevatedButtonColors(
                                            containerColor = when {
                                                showAsCorrect -> MaterialTheme.colorScheme.primaryContainer
                                                showAsIncorrect -> MaterialTheme.colorScheme.errorContainer
                                                isSelected -> MaterialTheme.colorScheme.secondaryContainer
                                                else -> MaterialTheme.colorScheme.surface
                                            }
                                        )
                                    ) {
                                        Text(
                                            text = option,
                                            color = when {
                                                showAsCorrect -> MaterialTheme.colorScheme.onPrimaryContainer
                                                showAsIncorrect -> MaterialTheme.colorScheme.onErrorContainer
                                                isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
                                                else -> MaterialTheme.colorScheme.onSurface
                                            }
                                        )
                                    }
                                }
                                if (showExplanation) {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = MaterialTheme.shapes.medium,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = question.explanation,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                        if (showExplanation) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (currentQuestionIndex > 0) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.previousQuestion()
                                            showExplanation = false
                                        }
                                    ) {
                                        Text("Назад")
                                    }
                                } else {
                                    Spacer(modifier = Modifier.width(96.dp))
                                }

                                if (currentQuestionIndex < currentQuiz.questions.size - 1) {
                                    Button(
                                        onClick = {
                                            viewModel.nextQuestion()
                                            showExplanation = false
                                        },
                                        enabled = selectedAnswers[currentQuestionIndex] != null
                                    ) {
                                        Text("Далее")
                                    }
                                } else {
                                    Button(
                                        onClick = { viewModel.submitQuiz() },
                                        enabled = selectedAnswers[currentQuestionIndex] != null
                                    ) {
                                        Text("Завершить")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    correctAnswers: Int,
    totalQuestions: Int,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "You got $correctAnswers out of $totalQuestions questions correct",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Score: ${(correctAnswers.toFloat() / totalQuestions * 100).toInt()}%",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish")
        }
    }
} 