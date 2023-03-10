package ai.bright.provbit.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.HiltAndroidApp

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@HiltAndroidApp
class MainApplication : HasActivity, Application(), Application.ActivityLifecycleCallbacks{

    // This is a bit of a bodge to support Auth0, which requires the activity context
    // to launch.
    override var currentActivity: Activity? = null

    init { registerActivityLifecycleCallbacks(this) }

    override fun onActivityStarted(activity: Activity) { if (activity is MainActivity) currentActivity = activity }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { }
    override fun onActivityResumed(activity: Activity) { }
    override fun onActivityPaused(activity: Activity) { }
    override fun onActivityStopped(activity: Activity) { }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }
    override fun onActivityDestroyed(activity: Activity) { }
}

interface HasActivity {
    val currentActivity: Activity?
    
}