package cm.avisingh.legalease.ui.notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cm.avisingh.legalease.R

class NotificationsActivity : AppCompatActivity() {

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        viewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notifications"

        // TODO: Setup RecyclerView, SwipeRefreshLayout, and observers when layouts are ready
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
