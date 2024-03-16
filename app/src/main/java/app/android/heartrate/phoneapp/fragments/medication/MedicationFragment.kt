package app.android.heartrate.phoneapp.fragments.medication

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.activities.record.AddProfileActivity
import app.android.heartrate.phoneapp.activities.record.MedicineDataActivity
import app.android.heartrate.phoneapp.databinding.FragmentMedicationsBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class MedicationFragment : BaseFragment() {

    private lateinit var binding: FragmentMedicationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedicationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        handleClicks()
    }

    private fun handleClicks() {
        binding.cvAddMedication.setOnClickListener {
            handlePermissions()
        }
    }

    private fun handlePermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            try {
                TedPermission.create().setPermissions(
                    "android.permission.READ_MEDIA_IMAGES",
                    "android.permission.CAMERA"
                ).setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        startActivity(MedicineDataActivity::class.java)
                    }

                    override fun onPermissionDenied(list: List<String>) {
                        Log.e("Permission:", "Permission Denied!")
                    }
                }).check()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        } else if (Build.VERSION.SDK_INT >= 30) {
            try {
                TedPermission.create().setPermissions(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.CAMERA"
                ).setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        startActivity(MedicineDataActivity::class.java)
                    }

                    override fun onPermissionDenied(list: List<String>) {
                        Log.e("Permission:", "Permission Denied!")
                    }
                }).check()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        } else {
            try {
                TedPermission.create().setPermissions(
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.CAMERA"
                ).setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        startActivity(MedicineDataActivity::class.java)
                    }

                    override fun onPermissionDenied(list: List<String>) {
                        Log.e("Permission:", "Permission Denied!")
                    }
                }).check()
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }


    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}