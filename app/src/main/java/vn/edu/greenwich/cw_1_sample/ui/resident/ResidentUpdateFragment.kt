package vn.edu.greenwich.cw_1_sample.ui.resident

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import vn.edu.greenwich.cw_1_sample.R
import vn.edu.greenwich.cw_1_sample.models.Resident
import vn.edu.greenwich.cw_1_sample.utils.serializable

class ResidentUpdateFragment : Fragment(R.layout.fragment_resident_update), ResidentRegisterFragment.FragmentListener {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		val view: View = inflater.inflate(R.layout.fragment_resident_update, container, false)

		if (arguments != null) {
			val resident = requireArguments().serializable<Resident>(ARG_PARAM_RESIDENT)
			val bundle = Bundle()

			bundle.putSerializable(ResidentRegisterFragment.ARG_PARAM_RESIDENT, resident)

			// Send arguments (resident info) to ResidentRegisterFragment.
			childFragmentManager.fragments[0].arguments = bundle
		}
		return view
	}

	override fun sendFromResidentRegisterFragment(status: Long) {
		when (status.toInt()) {
			0 -> Toast.makeText(context, R.string.notification_update_fail, Toast.LENGTH_SHORT).show()
			else -> {
				Toast.makeText(context, R.string.notification_update_success, Toast.LENGTH_SHORT).show()
				findNavController(requireView()).navigateUp()
			}
		}
	}

	companion object {
		const val ARG_PARAM_RESIDENT = "resident"
	}
}
