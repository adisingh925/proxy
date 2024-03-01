package app.android.heartrate.phoneapp.eventsmonitor

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.await
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.HealthEvent
import androidx.health.services.client.data.PassiveListenerConfig
import com.google.android.gms.wearable.DataMap.TAG
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class HealthServicesManager private constructor(context: Context) {

    private var healthServicesClient: HealthServicesClient
    private var passiveMonitoringClient: PassiveMonitoringClient
    private val healthEventTypes = setOf(HealthEvent.Type.FALL_DETECTED)
    private var registered: Boolean = false
    private var healthEventsObserver: HealthEventsObserver? = null

    companion object : SingletonHolder<HealthServicesManager, Context>(::HealthServicesManager)

    init {
        healthServicesClient = HealthServices.getClient(context)
        passiveMonitoringClient = healthServicesClient.passiveMonitoringClient
    }

    suspend fun hasHealthEventsCapability(): Boolean {
        val capabilities = passiveMonitoringClient.getCapabilitiesAsync().await()
        for (healthEventType in healthEventTypes) {
            if (healthEventType !in capabilities.supportedHealthEventTypes) {
                return false
            }
        }
        return true
    }

    suspend fun registerForHealthEventsData() {
        Log.i(TAG, "Registering listener")
        val passiveListenerConfig = PassiveListenerConfig.builder()
            .setHealthEventTypes(healthEventTypes)
            .build()

        passiveMonitoringClient.setPassiveListenerServiceAsync(
            PassiveHealthEventService::class.java,
            passiveListenerConfig
        ).await()
        registered = true
    }

    suspend fun unregisterHealthEventsData() {
        Log.i(TAG, "Unregistering listeners")
        passiveMonitoringClient.clearPassiveListenerServiceAsync().await()
        registered = false
    }

    fun isRegistered(): Boolean {
        return registered
    }

    fun setHealthEventsObserver(healthEventsObs: HealthEventsObserver) {
        healthEventsObserver = healthEventsObs
    }

    fun recordHealthEvent(healthEvent: HealthEvent) {
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.ENGLISH)
            .withZone(ZoneId.systemDefault())
        val eventData = EventData(healthEvent.type.name, formatter.format(healthEvent.eventTime))
        healthEventsObserver!!.onEventDataChanged(eventData)
    }
}