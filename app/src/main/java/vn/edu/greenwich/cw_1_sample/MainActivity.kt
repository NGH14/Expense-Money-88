package vn.edu.greenwich.cw_1_sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
	private lateinit var appBarConfiguration: AppBarConfiguration

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val navHostFragment = main_nav_host.getFragment<NavHostFragment>()
		val navController = navHostFragment.navController

		appBarConfiguration = AppBarConfiguration.Builder(
			R.id.residentListFragment,
			R.id.aboutUsFragment,
			R.id.testFragment
		).build()

		setupWithNavController(main_nav_bottom, navController)
		setupActionBarWithNavController(this, navController, appBarConfiguration)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_in_action, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
		android.R.id.home -> {
			onBackPressedDispatcher.onBackPressed()
			true
		}
		else -> super.onOptionsItemSelected(item)
	}
}
