package app.android.heartrate.phoneapp.listener

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class PhoneListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val intent = Intent("local-message")
        if (messageEvent.path == HEART_RATE_MESSAGE_PATH) {
            Log.d(TAG, "Heart rate received: ${String(messageEvent.data)}")
            intent.putExtra("heartRate", String(messageEvent.data))
        } else if (messageEvent.path == FALL_DETECTION_MESSAGE_PATH) {
            Log.d(TAG, "Fall detected: ${String(messageEvent.data)}")
            intent.putExtra("fallDetected", String(messageEvent.data))
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "PhoneListenerService"
        private const val HEART_RATE_MESSAGE_PATH = "/heart_rate"
        private const val FALL_DETECTION_MESSAGE_PATH = "/fall_detection"
    }
}
