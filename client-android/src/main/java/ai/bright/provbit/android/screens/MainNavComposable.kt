package ai.bright.provbit.android.screens

import ai.bright.provbit.android.ProcessorViewModel
import ai.bright.provbit.demo.SharedComponent
import ai.bright.provbit.demo.presentation.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavViewModel @Inject constructor(
    sc: SharedComponent
) : ProcessorViewModel<NavViewData, NavEvent>(
    sc.navProcessor()
)

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Composable
fun MainNavComposable(
    vm: MainNavViewModel = hiltViewModel()
) {

    val navController = rememberAnimatedNavController()

    val navState by vm.viewData.collectAsState()
    ProvbitThemed(theme = navState.theme) {
        AnimatedNavHost(navController = navController, startDestination = "splashNav") {

            navigation("splash", route = "splashNav") {
                composable("splash") { SplashComposable() }
            }

            navigation("login", route = "loginNav") {
                composable("login") {
                    SignInComposable {
                        when (it) {
                            SignInEvent.Dismiss -> navController.navigate("form")
                        }
                    }
                }

                composable("form",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    FormComposable {
                        when (it) {
                            FormEvent.SubmitForm -> println("Form submitted!")
                        }
                    }
                }
            }


            navigation("home", route = "mainApp") {
                composable("home") {
                    AppInfoComposable {
                        when (it) {
                            AppInfoEvent.ToList -> navController.navigate("list")
                            AppInfoEvent.ToSelector -> navController.navigate("selector")
                            AppInfoEvent.ToQRResults -> navController.navigate("qrscan")
                            AppInfoEvent.ToStyleGuide -> navController.navigate("styleGuide")
                            AppInfoEvent.ToImageAnalysis -> navController.navigate("imgAnalysis")
                            AppInfoEvent.ToBluetooth -> navController.navigate("bluetoothScan")
                        }
                    }
                }

                composable("selector",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    MultiSelectorDemoComposable()
                }
                composable("list",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    ItemListComposable {
                        when (it) {
                            is ItemListEvent.ToDetail -> navController.navigate("list/${it.itemGuid}")
                        }
                    }
                }

                composable("list/{itemGuid}",
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) },
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) }
                )
                {
                    ItemDetailComposable {
                        when (it) {
                            ItemDetailEvent.Back -> navController.popBackStack()
                        }
                    }
                }

                composable("selector",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    MultiSelectorDemoComposable()
                }

                composable("styleGuide",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    StyleGuideComposable()
                }

                composable("imgAnalysis",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    ImageAnalysisComposable()
                }

                composable("bluetoothScan",
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    BluetoothScanComposable()
                }

            }
            composable("qrscan") {
                QRScanScreen()
            }
        }
        /**
         * Overarching nav state indicates one of three states. This clears the full
         * stack and sets a single graph as the root. In all cases a 'back press'
         * will exit the app.
         */
        /**
         * Overarching nav state indicates one of three states. This clears the full
         * stack and sets a single graph as the root. In all cases a 'back press'
         * will exit the app.
         */
        val parentNavGraph = navController.currentDestination?.parent?.route
        val targetNavGraph = when (navState.state) {
            NavState.Splash -> "splashNav"
            NavState.Unauthenticated -> "loginNav"
            NavState.Authenticated -> "mainApp"
        }
        if (targetNavGraph != parentNavGraph) {
            navController.navigate(targetNavGraph) {
                launchSingleTop = true
                parentNavGraph?.also {
                    popUpTo(it) { inclusive = true }
                }
            }
        }
    }
}
