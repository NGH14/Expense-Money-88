package vn.edu.greenwich.cw_1_sample.ui.request

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import vn.edu.greenwich.cw_1_sample.models.Request
import vn.edu.greenwich.cw_1_sample.ui.dialog.DatePickerFragment
import vn.edu.greenwich.cw_1_sample.ui.dialog.TimePickerFragment
import android.R as androidR
import vn.edu.greenwich.cw_1_sample.R as appR

class RequestCreateFragment(residentId: Long? = null) :
	DialogFragment(appR.layout.fragment_request_create),
	DatePickerFragment.FragmentListener,
	TimePickerFragment.FragmentListener {

	private lateinit var fmRequestCreateDate: EditText
	private lateinit var fmRequestCreateTime: EditText
	private lateinit var fmRequestCreateContent: EditText
	private lateinit var fmRequestCreateButtonCancel: Button
	private lateinit var fmRequestCreateButtonAdd: Button
	private lateinit var fmRequestCreateType: Spinner

	override fun sendFromDatePickerFragment(date: String?) {
		fmRequestCreateDate.setText(date)
	}

	override fun sendFromTimePickerFragment(time: String?) {
		fmRequestCreateTime.setText(time)
	}

	override fun onResume() {
		super.onResume()

		dialog!!.window!!.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		val view: View = inflater.inflate(appR.layout.fragment_request_create, container, false)

		fmRequestCreateDate = view.findViewById(appR.id.fmRequestCreateDate)
		fmRequestCreateDate.setOnTouchListener { _: View?, motionEvent: MotionEvent -> showDateDialog(motionEvent) }

		fmRequestCreateTime = view.findViewById(appR.id.fmRequestCreateTime)
		fmRequestCreateTime.setOnTouchListener { _: View?, motionEvent: MotionEvent -> showTimeDialog(motionEvent) }

		fmRequestCreateButtonAdd = view.findViewById(appR.id.fmRequestCreateButtonAdd)
		fmRequestCreateButtonAdd.setOnClickListener { createRequest() }

		fmRequestCreateButtonCancel = view.findViewById(appR.id.fmRequestCreateButtonCancel)
		fmRequestCreateButtonCancel.setOnClickListener { dismiss() }

		fmRequestCreateType = view.findViewById(appR.id.fmRequestCreateType)
		fmRequestCreateContent = view.findViewById(appR.id.fmRequestCreateContent)

		setTypeSpinner()

		return view
	}

	private fun setTypeSpinner() {
		val adapter = ArrayAdapter.createFromResource(
			requireContext(),
			appR.array.request_type,
			androidR.layout.simple_spinner_item
		)
		adapter.setDropDownViewResource(androidR.layout.simple_spinner_dropdown_item)
		fmRequestCreateType.adapter = adapter
	}

	private fun showDateDialog(motionEvent: MotionEvent): Boolean {
		var show = false

		if (motionEvent.action == MotionEvent.ACTION_DOWN) {
			DatePickerFragment().show(childFragmentManager, null)
			show = true
		}

		return show
	}

	private fun showTimeDialog(motionEvent: MotionEvent): Boolean {
		if (motionEvent.action == MotionEvent.ACTION_DOWN) {
			TimePickerFragment().show(childFragmentManager, null)
			return true
		}
		return false
	}

	private fun createRequest() {
		val request = Request()
		with(request) {
			type = fmRequestCreateType.selectedItem.toString()
			time = fmRequestCreateType.selectedItem.toString()
			date = fmRequestCreateType.selectedItem.toString()
			content = fmRequestCreateType.selectedItem.toString()
		}

		(parentFragment as FragmentListener?)?.sendFromRequestCreateFragment(request)
		dismiss()
	}

	interface FragmentListener {
		fun sendFromRequestCreateFragment(request: Request?)
	}
}
