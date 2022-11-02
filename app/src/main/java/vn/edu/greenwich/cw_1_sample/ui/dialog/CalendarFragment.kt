package vn.edu.greenwich.cw_1_sample.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.DialogFragment
import vn.edu.greenwich.cw_1_sample.R

class CalendarFragment : DialogFragment() {
	private lateinit var fmCalendarCalendar: CalendarView

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		val view = inflater.inflate(R.layout.fragment_calendar, container, false)

		fmCalendarCalendar = view.findViewById(R.id.fmCalendarCalendar)
		fmCalendarCalendar.setOnDateChangeListener { _: CalendarView?, year: Int, month: Int, day: Int ->
			/** DEBUG
			 * old: ++month
			 * remarks: idk why the old code can modify lambda parameter (month)
			 */
			val theMonthAfter = month + 1
			val date = """
				${if (day < 10) "0$day" else day}/
				${if (theMonthAfter < 10) "0$theMonthAfter" else theMonthAfter}/
				$year
			""".trimIndent().trimMargin()

			// listener
			(parentFragment as FragmentListener?)?.sendFromCalendarFragment(date)

			dismiss()
		}

		return view
	}

	interface FragmentListener {
		fun sendFromCalendarFragment(date: String?)
	}
}
