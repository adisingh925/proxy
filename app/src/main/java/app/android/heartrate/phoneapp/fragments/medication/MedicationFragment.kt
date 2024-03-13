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
import androidx.fragment.app.Fragment
import app.android.heartrate.phoneapp.R
import app.android.heartrate.phoneapp.activities.record.AddMedicinesActivity
import app.android.heartrate.phoneapp.activities.record.AddProfileActivity
import app.android.heartrate.phoneapp.activities.record.MedicineDataActivity
import app.android.heartrate.phoneapp.databinding.FragmentMedicationsBinding
import app.android.heartrate.phoneapp.databinding.FragmentWellnessBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.medication.add.AddMedicationActivity
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
    ): View? {
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

            val sqlite = SQLiteHealthTracker(requireContext()).open()
            val exist = sqlite.CheckProfileDataExist()
            if (exist) {
                startActivity(MedicineDataActivity::class.java)
            } else {
                NoProfileDialog()
            }
        }
    }

    private fun NoProfileDialog() {
        val dialog = Dialog(requireContext(), R.style.TransparentBackground)
        dialog.requestWindowFeature(1)
        dialog.setContentView(R.layout.dialog_rate)
        val button = dialog.findViewById<Button>(R.id.dialog_conform_btn_yes)
        val button2 = dialog.findViewById<Button>(R.id.dialog_conform_btn_no)
        val str: String = AppConstants.create_profile_messages
        (dialog.findViewById<View>(R.id.dialog_conform_txt_header) as TextView).text =
            "User Profile"
        (dialog.findViewById<View>(R.id.dialog_conform_txt_message) as TextView).text =
            str
        button.text = "Create"
        button2.text = "Cancel"
        button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 33) {
                try {
                    TedPermission.create().setPermissions(
                        "android.permission.READ_MEDIA_IMAGES",
                        "android.permission.CAMERA"
                    ).setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            startActivity(AddProfileActivity::class.java)
                        }

                        override fun onPermissionDenied(list: List<String>) {
                            Log.e("Permission:", "Permission Denied!")
                        }
                    }).check()
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            } else if (Build.VERSION.SDK_INT >= 30) {
                try {
                    TedPermission.create().setPermissions(
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.CAMERA"
                    ).setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            startActivity(AddProfileActivity::class.java)
                        }

                        override fun onPermissionDenied(list: List<String>) {
                            Log.e("Permission:", "Permission Denied!")
                        }
                    }).check()
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            } else {
                try {
                    TedPermission.create().setPermissions(
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE",
                        "android.permission.CAMERA"
                    ).setPermissionListener(object : PermissionListener {
                        override fun onPermissionGranted() {
                            startActivity(AddProfileActivity::class.java)
                        }

                        override fun onPermissionDenied(list: List<String>) {
                            Log.e("Permission:", "Permission Denied!")
                        }
                    }).check()
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                dialog.dismiss()
            }
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}