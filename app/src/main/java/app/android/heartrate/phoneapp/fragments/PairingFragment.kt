package app.android.heartrate.phoneapp.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import app.android.heartrate.phoneapp.databinding.FragmentPairingBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PairingFragment : Fragment() {

    private val binding by lazy {
        FragmentPairingBinding.inflate(layoutInflater)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val heartRate = intent.getStringExtra("heartRate")
            if (heartRate != null) {
                binding.heartrateValue.text = heartRate
            }

            val fallDetected = intent.getStringExtra("fallDetected")
            if (fallDetected != null) {
                binding.fallDetected.text = "Fall Detected"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val filter = IntentFilter("local-message")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter)

        return binding.root
    }
}