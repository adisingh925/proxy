package app.android.heartrate.phoneapp.eventsmonitor

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.wearable.DataMap.TAG
import kotlinx.coroutines.runBlocking

class StartupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        runBlocking {
            val result = context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
            if (result == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Enqueuing worker")
                WorkManager.getInstance(context).enqueue(
                    OneTimeWorkRequestBuilder<RegisterForBackgroundDataWorker>().build()
                )
            }
        }
    }
}