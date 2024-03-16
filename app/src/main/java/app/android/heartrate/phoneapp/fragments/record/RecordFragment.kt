package app.android.heartrate.phoneapp.fragments.record

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
import app.android.heartrate.phoneapp.activities.record.TrackerStatisticsActivity
import app.android.heartrate.phoneapp.activities.record.TrackerToolsActivity
import app.android.heartrate.phoneapp.databinding.FragmentRecordBinding
import app.android.heartrate.phoneapp.fragments.base.BaseFragment
import app.android.heartrate.phoneapp.fragments.record.add.AddRecordActivity
import app.android.heartrate.phoneapp.sqlite.SQLiteHealthTracker
import app.android.heartrate.phoneapp.utils.AppConstants
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class RecordFragment : BaseFragment() {

    private lateinit var binding: FragmentRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(layoutInflater)
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
        binding.cvAddRecord.setOnClickListener {
            startActivity(AddRecordActivity::class.java)
        }


        binding.cvHealthTracker.setOnClickListener {
            val sqlite = SQLiteHealthTracker(requireContext()).open()
            val exist = sqlite.CheckProfileDataExist()
            if (exist) {
                startActivity(TrackerToolsActivity::class.java)
            } else {
                NoProfileDialog()
            }
        }

        binding.cvStatistics.setOnClickListener {
            val sqlite = SQLiteHealthTracker(requireContext()).open()
            val exist = sqlite.CheckProfileDataExist()
            if (exist) {
                startActivity(TrackerStatisticsActivity::class.java)
            } else {
                NoProfileDialog()
            }
        }

        binding.cvAddUser.setOnClickListener {
            NoProfileDialog()
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
        }
        button2.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun startActivity(cls: Class<*>) {
        startActivity(Intent(requireContext(), cls))
    }
}