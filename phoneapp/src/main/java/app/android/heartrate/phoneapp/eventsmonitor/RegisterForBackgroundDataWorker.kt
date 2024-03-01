package app.android.heartrate.phoneapp.eventsmonitor

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.android.heartrate.phoneapp.eventsmonitor.HealthServicesManager
import com.google.android.gms.wearable.DataMap.TAG
import kotlinx.coroutines.runBlocking

class RegisterForBackgroundDataWorker constructor(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        Log.i(TAG, "Worker running")
        runBlocking {
            if (!HealthServicesManager.getInstance(applicationContext).isRegistered()
                && HealthServicesManager.getInstance(applicationContext).hasHealthEventsCapability()
            ) {
                HealthServicesManager.getInstance(applicationContext).registerForHealthEventsData()
            }
        }
        return Result.success()
    }
}