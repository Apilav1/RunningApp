package com.runningapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.runningapp.R
import com.runningapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //if the activity was destroyed and click on notification start it again
        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        bottomNavigationView.setOnNavigationItemReselectedListener {
            //no op for reclicked item
        }

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                        bottomNavigationView.visibility = View.VISIBLE
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //for when user clicks on notification to open TrackingFragment
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }
}
