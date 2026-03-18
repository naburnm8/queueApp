package ru.naburnm8.queueapp.queueOperator.metrics.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.naburnm8.queueapp.queueOperator.metrics.navigation.StudentMetricsNavigation
import ru.naburnm8.queueapp.queueOperator.metrics.viewmodel.StudentMetricsViewmodel
import ru.naburnm8.queueapp.queueOperator.navigation.QueueOperatorFlowNavigation

fun NavGraphBuilder.studentMetricsFlow(
    navController: NavHostController
) {
    navigation(
        route = QueueOperatorFlowNavigation.Metrics.name,
        startDestination = StudentMetricsNavigation.MetricsMain.name
    ) {
        composable (StudentMetricsNavigation.MetricsMain.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Metrics.name)
            }

            val metricsVm: StudentMetricsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            StudentMetricsListScreen(
                modifier = Modifier.fillMaxSize(),
                vm = metricsVm,
                onNavigateToMetric = {
                    navController.navigate(StudentMetricsNavigation.MetricItemView.name)
                },
                onNavigateToNewMetric = {
                    navController.navigate(StudentMetricsNavigation.CreateMetric.name)
                }
            )

        }

        composable (StudentMetricsNavigation.CreateMetric.name) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Metrics.name)
            }

            val metricsVm: StudentMetricsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            StudentMetricsCreateScreen(
                modifier = Modifier.fillMaxSize(),
                vm = metricsVm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable  (StudentMetricsNavigation.MetricItemView.name ) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(QueueOperatorFlowNavigation.Metrics.name)
            }

            val metricsVm: StudentMetricsViewmodel = koinViewModel(viewModelStoreOwner = parentEntry)

            StudentMetricsEditScreen(
                modifier = Modifier.fillMaxSize(),
                vm = metricsVm,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}