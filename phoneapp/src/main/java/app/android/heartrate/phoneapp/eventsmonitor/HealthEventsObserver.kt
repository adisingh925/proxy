package app.android.heartrate.phoneapp.eventsmonitor

interface HealthEventsObserver {

    fun onEventDataChanged(eventData: EventData)
}