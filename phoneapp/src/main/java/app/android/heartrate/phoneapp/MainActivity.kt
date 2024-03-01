package app.android.heartrate.phoneapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.android.heartrate.phoneapp.databinding.ActivityMainBinding
import app.android.heartrate.phoneapp.eventsmonitor.HealthServicesManager
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import app.android.heartrate.phoneapp.eventsmonitor.EventData
import app.android.heartrate.phoneapp.eventsmonitor.HealthEventsObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var transcriptionNodeId: String? = null
    private val sensorManager by lazy { this.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val heartRateSensor: Sensor? by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) }
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var permissionGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST)

        permissionGranted = applicationContext.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                permissionGranted = result
            }

        HealthServicesManager.getInstance(applicationContext)
            .setHealthEventsObserver(healthEventsObserver)

        if (!getPermissionGranted()) {
            requestPermissions()
            return
        }

        onSubscribeButtonClick()
    }

    private fun getNodes(): Collection<String> {
        return Tasks.await(Wearable.getNodeClient(this).connectedNodes).map { it.id }
    }

    private fun sendMessage(data: String, path: String) {
        CoroutineScope(Dispatchers.IO).launch {
            transcriptionNodeId = getNodes().first().also { nodeId ->
                Wearable.getMessageClient(applicationContext).sendMessage(
                    nodeId,
                    path,
                    data.toByteArray()
                ).apply {
                    addOnSuccessListener { Log.d(TAG, "OnSuccess") }
                    addOnFailureListener { Log.d(TAG, "OnFailure") }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainWearActivity"
        private const val HEART_RATE_MESSAGE_PATH = "/heart_rate"
        private const val FALL_DETECTION_MESSAGE_PATH = "/fall_detection"
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
            Log.d(TAG, "Heart rate: ${event.values[0]}")
            sendMessage(event.values[0].toString(), HEART_RATE_MESSAGE_PATH)
            binding.heartRate.text = event.values[0].toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "Accuracy changed")
    }

    private var healthEventsObserver: HealthEventsObserver = object : HealthEventsObserver {
        override fun onEventDataChanged(eventData: EventData) {
            runOnUiThread {
                Log.d(TAG, "Event data changed")
                binding.eventDetection.text = eventData.eventType
                sendMessage(eventData.eventType, FALL_DETECTION_MESSAGE_PATH)
            }
        }
    }

    private fun onSubscribeButtonClick() {
        if (!getPermissionGranted()) {
            Toast.makeText(
                applicationContext,
                "Please grant all permissions!",
                Toast.LENGTH_LONG
            ).show()
            requestPermissions()
            return
        }

        lifecycleScope.launch {
            if (!HealthServicesManager.getInstance(applicationContext)
                    .hasHealthEventsCapability()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Some Health events not available",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }
            HealthServicesManager.getInstance(applicationContext).registerForHealthEventsData()
            Log.d(TAG, "Subscribed")
        }
    }

    private fun getPermissionGranted(): Boolean {
        return permissionGranted
    }

    private fun requestPermissions() {
        permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
    }
}