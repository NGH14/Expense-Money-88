package vn.edu.greenwich.cw_1_sample.ui.resident

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_resident_register_confirm.*
import vn.edu.greenwich.cw_1_sample.R
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO
import vn.edu.greenwich.cw_1_sample.models.Resident
import vn.edu.greenwich.cw_1_sample.utils.setWidthPercent

class ResidentRegisterConfirmFragment(var resident: Resident? = null) :
	DialogFragment(R.layout.fragment_resident_register_confirm) {

	private var _db: ResimaDAO? = null
	private var _resident: Resident = resident ?: Resident()

	override fun onAttach(context: Context) {
		super.onAttach(context)

		_db = ResimaDAO(getContext())
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setWidthPercent(85)

		fmResidentRegisterConfirmName.text = when {
			_resident.name?.trim()?.isNotEmpty() == false -> getString(R.string.error_no_info)
			else -> _resident.name
		}
		fmResidentRegisterConfirmStartDate.text = when {
			_resident.startDate?.trim()?.isNotEmpty() == false -> getString(R.string.error_no_info)
			else -> _resident.startDate
		}
		fmResidentRegisterConfirmOwner.text = when (_resident.owner) {
			-1 -> getString(R.string.error_no_info)
			1 -> getString(R.string.label_owner)
			else -> getString(R.string.label_tenant)
		}

		fmResidentRegisterConfirmButtonCancel.setOnClickListener { dismiss() }
		fmResidentRegisterConfirmButtonConfirm.setOnClickListener { confirm() }
	}

	private fun confirm() {
		val status = _db?.insertResident(_resident)
		status?.let { (parentFragment as FragmentListener?)?.sendFromResidentRegisterConfirmFragment(it) }
		dismiss()
	}

	interface FragmentListener {
		fun sendFromResidentRegisterConfirmFragment(status: Long)
	}
}
