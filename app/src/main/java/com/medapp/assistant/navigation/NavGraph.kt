package com.medapp.assistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.medapp.assistant.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        
        composable(Screen.MedicineList.route) {
            MedicineListScreen(navController)
        }
        
        composable(
            route = Screen.MedicineDetail.route,
            arguments = listOf(
                navArgument("medicineName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicineName = backStackEntry.arguments?.getString("medicineName") ?: return@composable
            MedicineDetailScreen(medicineName = medicineName, navController = navController)
        }
        
        composable(Screen.PersonalMedicines.route) {
            PersonalMedicinesScreen(navController)
        }
        
        composable(Screen.ExpiringMedicines.route) {
            ExpiringMedicinesScreen(navController)
        }
        
        composable(Screen.FirstAidGuides.route) {
            FirstAidGuidesScreen(navController)
        }
        
        composable(
            route = Screen.FirstAidGuides.route + "/{guideId}",
            arguments = listOf(
                navArgument("guideId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val guideId = backStackEntry.arguments?.getLong("guideId") ?: return@composable
            GuideDetailScreen(navController = navController, guideId = guideId)
        }
        
        composable(Screen.EmergencyGuides.route) {
            EmergencyGuidesScreen(navController)
        }
        
        composable(Screen.QuizList.route) {
            QuizListScreen(navController)
        }
        
        composable(
            route = Screen.QuizDetail.route + "/{quizId}",
            arguments = listOf(
                navArgument("quizId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val quizId = backStackEntry.arguments?.getLong("quizId") ?: return@composable
            QuizDetailScreen(navController = navController, quizId = quizId)
        }
        
        composable(Screen.QuizResults.route) {
            QuizResultsScreen(navController)
        }
        
        composable(Screen.InstructionList.route) {
            InstructionListScreen(navController)
        }
        
        composable(
            route = "instruction_detail/{instructionNumber}",
            arguments = listOf(navArgument("instructionNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val instructionNumber = backStackEntry.arguments?.getInt("instructionNumber") ?: 1
            InstructionDetailScreen(instructionNumber = instructionNumber, navController = navController)
        }
    }
} 