package vn.edu.greenwich.cw_1_sample.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_setting.*
import vn.edu.greenwich.cw_1_sample.R
import vn.edu.greenwich.cw_1_sample.database.BackupEntry
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO
import vn.edu.greenwich.cw_1_sample.models.Backup
import vn.edu.greenwich.cw_1_sample.ui.resident.list.ResidentListFragment
import java.util.*

class SettingFragment : Fragment(R.layout.fragment_setting) {

	private lateinit var _db: ResimaDAO

	override fun onAttach(context: Context) {
		super.onAttach(context)
		_db = ResimaDAO(context)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		settingBackup.setOnClickListener { backup() }
		settingResetDatabase.setOnClickListener { resetDatabase() }
		settingDarkMode.setOnClickListener { toggleAppTheme() }
	}

	private fun toggleAppTheme() {
		val modes = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

		if (modes == Configuration.UI_MODE_NIGHT_UNDEFINED) return

		when (modes) {
			Configuration.UI_MODE_NIGHT_YES -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
			Configuration.UI_MODE_NIGHT_NO -> {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			}
		}
	}

	private fun backup() {
		val residents = _db.getResidentList(null, null, false)
		val requests = _db.getRequestList(null, null, false)

		if (residents.isNotEmpty() && requests.isNotEmpty()) {
			val deviceName =
				"${Build.MANUFACTURER} " +
					"${Build.MODEL} " +
					"${Build.VERSION.RELEASE} " +
					Build.VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name
			val backup = Backup(Date(), deviceName, residents, requests)

			FirebaseFirestore.getInstance()
				.collection(BackupEntry.TABLE_NAME)
				.add(backup)
				.addOnSuccessListener { document ->
					Toast.makeText(context, R.string.notification_backup_success, Toast.LENGTH_SHORT).show()
					Log.d(resources.getString(R.string.label_backup_firestore), document.id)
				}
				.addOnFailureListener { e ->
					Toast.makeText(context, R.string.notification_backup_fail, Toast.LENGTH_SHORT).show()
					e.printStackTrace()
				}

			return
		}
		Toast.makeText(context, R.string.error_empty_list, Toast.LENGTH_SHORT).show()
	}

	private fun resetDatabase() {
		_db.reset()
		Toast.makeText(context, R.string.label_reset_database, Toast.LENGTH_SHORT).show()
	}
}
