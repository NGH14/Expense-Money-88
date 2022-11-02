package vn.edu.greenwich.cw_1_sample.ui.resident

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import vn.edu.greenwich.cw_1_sample.R
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO
import vn.edu.greenwich.cw_1_sample.models.Request
import vn.edu.greenwich.cw_1_sample.models.Resident
import vn.edu.greenwich.cw_1_sample.ui.dialog.DeleteConfirmFragment
import vn.edu.greenwich.cw_1_sample.ui.request.RequestCreateFragment
import vn.edu.greenwich.cw_1_sample.ui.request.list.RequestListFragment
import vn.edu.greenwich.cw_1_sample.utils.serializable

class ResidentDetailFragment :
	Fragment(R.layout.fragment_resident_detail),
	DeleteConfirmFragment.FragmentListener,
	RequestCreateFragment.FragmentListener {

	private lateinit var _db: ResimaDAO
	private var _resident: Resident? = null
	private lateinit var fmResidentDetailRequestButton: Button
	private lateinit var fmResidentDetailBottomAppBar: BottomAppBar
	private lateinit var fmResidentDetailRequestList: FragmentContainerView
	private lateinit var fmResidentDetailName: TextView
	private lateinit var fmResidentDetailStartDate: TextView
	private lateinit var fmResidentDetailOwner: TextView

	override fun onAttach(context: Context) {
		super.onAttach(context)

		_db = ResimaDAO(getContext())
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		val view = inflater.inflate(R.layout.fragment_resident_detail, container, false)

		fmResidentDetailName = view.findViewById(R.id.fmResidentDetailName)
		fmResidentDetailStartDate = view.findViewById(R.id.fmResidentDetailStartDate)
		fmResidentDetailOwner = view.findViewById(R.id.fmResidentDetailOwner)

		fmResidentDetailBottomAppBar = view.findViewById(R.id.fmResidentDetailBottomAppBar)
		fmResidentDetailBottomAppBar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener(::menuItemSelected))

		fmResidentDetailRequestButton = view.findViewById(R.id.fmResidentDetailRequestButton)
		fmResidentDetailRequestButton.setOnClickListener { showAddRequestFragment() }

		fmResidentDetailRequestList = view.findViewById(R.id.fmResidentDetailRequestList)

		showDetails()
		showRequestList()

		return view
	}

	private fun showDetails() {
		var name = getString(R.string.error_not_found)
		var startDate = getString(R.string.error_not_found)
		var owner = getString(R.string.error_not_found)

		if (arguments != null) {
			_resident = requireArguments().serializable(ARG_PARAM_RESIDENT)
			_resident = _resident?.let { _db.getResidentById(it.id) } // Retrieve data from Database.

			_resident?.name?.let { name = it }
			_resident?.startDate?.let { startDate = it }
			owner = if (_resident?.owner == 1) getString(R.string.label_owner) else getString(R.string.label_tenant)
		}

		fmResidentDetailName.text = name
		fmResidentDetailStartDate.text = startDate
		fmResidentDetailOwner.text = owner
	}

	private fun showRequestList() {
		if (arguments != null) {
			val bundle = Bundle()
			bundle.putSerializable(RequestListFragment.ARG_PARAM_RESIDENT_ID, _resident?.id)

			// Send arguments (resident id) to RequestListFragment.
			childFragmentManager.fragments[0].arguments = bundle
		}
	}

	private fun menuItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.residentUpdateFragment -> showUpdateFragment()
			R.id.residentDeleteFragment -> showDeleteConfirmFragment()
		}
		return true
	}

	private fun showUpdateFragment() {
		var bundle: Bundle? = null

		if (_resident != null) {
			bundle = Bundle()
			bundle.putSerializable(ResidentUpdateFragment.ARG_PARAM_RESIDENT, _resident)
		}

		findNavController(requireView()).navigate(R.id.residentUpdateFragment, bundle)
	}

	private fun showDeleteConfirmFragment() =
		DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(childFragmentManager, null)

	private fun showAddRequestFragment() {
		_resident?.id?.let { RequestCreateFragment(it).show(childFragmentManager, null) }
	}

	override fun sendFromDeleteConfirmFragment(status: Int) {
		if (status == 1 && _resident?.id != null) {
			val numOfDeletedRows = _db.deleteResident(_resident!!.id)

			if (numOfDeletedRows > 0) {
				Toast.makeText(context, R.string.notification_delete_success, Toast.LENGTH_SHORT).show()
				view?.let { findNavController(it).navigateUp() }

				return
			}
		}

		Toast.makeText(context, R.string.notification_delete_fail, Toast.LENGTH_SHORT).show()
	}

	override fun sendFromRequestCreateFragment(request: Request?) {
		if (request == null) return

		_resident?.id?.let { request.residentId = it }

		val id = _db.insertRequest(request)

		val text = if (id == -1L) R.string.notification_create_fail else R.string.notification_create_success
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

		reloadRequestList()
	}

	private fun reloadRequestList() {
		val bundle = Bundle()

		bundle.putSerializable(RequestListFragment.ARG_PARAM_RESIDENT_ID, _resident?.id)
		childFragmentManager.beginTransaction()
			.setReorderingAllowed(true)
			.replace(R.id.fmResidentDetailRequestList, RequestListFragment::class.java, bundle)
			.commit()
	}

	companion object {
		const val ARG_PARAM_RESIDENT = "resident"
	}
}
