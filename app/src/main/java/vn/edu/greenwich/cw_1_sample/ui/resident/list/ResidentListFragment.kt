package vn.edu.greenwich.cw_1_sample.ui.resident.list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.greenwich.cw_1_sample.R
import vn.edu.greenwich.cw_1_sample.database.ResimaDAO
import vn.edu.greenwich.cw_1_sample.models.Resident
import vn.edu.greenwich.cw_1_sample.ui.resident.ResidentSearchFragment

class ResidentListFragment : Fragment(R.layout.fragment_resident_list), ResidentSearchFragment.FragmentListener {
	private var _db: ResimaDAO? = null
	private var residentList = ArrayList<Resident>()
	private var residentAdapter: ResidentAdapter? = null
	private lateinit var fmResidentListFilter: EditText
	private lateinit var fmResidentListEmptyNotice: TextView
	private lateinit var fmResidentListRecyclerView: RecyclerView
	private lateinit var fmResidentListButtonSearch: ImageButton
	private lateinit var fmResidentListButtonResetSearch: ImageButton

	override fun onAttach(context: Context) {
		super.onAttach(context)

		_db = ResimaDAO(context)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View = inflater.inflate(R.layout.fragment_resident_list, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		residentAdapter = ResidentAdapter(residentList)

		fmResidentListRecyclerView = view.findViewById(R.id.fmResidentListRecyclerView)
		fmResidentListRecyclerView.adapter = residentAdapter
		fmResidentListRecyclerView.layoutManager = LinearLayoutManager(context)
		val dividerItemDecoration =
			DividerItemDecoration(context, /* orientation = */ LinearLayoutManager(context).orientation)
		fmResidentListRecyclerView.addItemDecoration(dividerItemDecoration)

		fmResidentListEmptyNotice = view.findViewById(R.id.fmResidentListEmptyNotice)

		fmResidentListButtonResetSearch = view.findViewById(R.id.fmResidentListButtonResetSearch)
		fmResidentListButtonResetSearch.setOnClickListener { resetSearch() }

		fmResidentListButtonSearch = view.findViewById(R.id.fmResidentListButtonSearch)
		fmResidentListButtonSearch.setOnClickListener { showSearchDialog() }

		fmResidentListFilter = view.findViewById(R.id.fmResidentListFilter)
		fmResidentListFilter.addTextChangedListener(filter())
	}

	override fun onResume() {
		super.onResume()

		reloadList(null)
	}

	private fun reloadList(resident: Resident?) {
		_db?.let { residentList = it.getResidentList(resident, null, false) }
		residentAdapter?.updateList(residentList)

		// Show "No Resident." message.
		fmResidentListEmptyNotice.visibility = if (residentList.isEmpty()) View.VISIBLE else View.GONE
	}

	private fun filter(): TextWatcher {
		return object : TextWatcher {
			override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
				residentAdapter!!.filter.filter(charSequence.toString())
			}

			override fun afterTextChanged(editable: Editable) {}
		}
	}

	private fun resetSearch() {
		fmResidentListFilter.setText("")
		reloadList(null)
	}

	private fun showSearchDialog() {
		ResidentSearchFragment().show(childFragmentManager, null)
	}

	override fun sendFromResidentSearchFragment(resident: Resident?) {
		reloadList(if (resident?.isEmpty() == false) resident else null)
	}
}
