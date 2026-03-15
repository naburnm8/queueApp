package ru.naburnm8.queueapp.queueOperator.metrics.ui

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.naburnm8.queueapp.queueOperator.metrics.navigation.StudentMetricsNavigation
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

fun NavGraphBuilder.studentMetricsFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueOperatorFlowNavigation.Metrics.name,
        startDestination = StudentMetricsNavigation.MetricsMain.name
    ) {
        composable (StudentMetricsNavigation.MetricsMain.name) {
            Text("Student Metrics")
        }
    }
}