package app.android.heartrate.phoneapp.eventsmonitor

import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.HealthEvent
import app.android.heartrate.phoneapp.eventsmonitor.HealthServicesManager
import com.google.android.gms.wearable.DataMap.TAG
import kotlinx.coroutines.runBlocking

class PassiveHealthEventService : PassiveListenerService() {

    override fun onHealthEventReceived(event: HealthEvent) {
        runBlocking {
            Log.i(TAG, "onHealthEventReceived received with type: ${event.type}")
            HealthServicesManager.getInstance(applicationContext).recordHealthEvent(event)
            super.onHealthEventReceived(event)
        }
    }
}