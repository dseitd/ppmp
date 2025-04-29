package com.medapp.assistant.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MedicineList : Screen("medicine_list")
    object MedicineDetail : Screen("medicine_detail/{medicineName}") {
        fun createRoute(medicineName: String) = "medicine_detail/$medicineName"
    }
    object FirstAidGuides : Screen("first_aid_guides")
    object QuizList : Screen("quiz_list")
    object QuizDetail : Screen("quiz_detail/{quizId}") {
        fun createRoute(quizId: Long) = "quiz_detail/$quizId"
    }
    object MedicineTracker : Screen("medicine_tracker")
    object PersonalMedicines : Screen("personal_medicines")
    object ExpiringMedicines : Screen("expiring_medicines")
    object QuizResults : Screen("quiz_results")
    object EmergencyGuides : Screen("emergency_guides")
    object InstructionList : Screen("instruction_list")
} 