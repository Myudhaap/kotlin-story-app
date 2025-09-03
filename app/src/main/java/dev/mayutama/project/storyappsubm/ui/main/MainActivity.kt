package dev.mayutama.project.storyappsubm.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.databinding.ActivityMainBinding
import dev.mayutama.project.storyappsubm.ui.storyAdd.StoryAddActivity
import dev.mayutama.project.storyappsubm.util.disableScreenAction
import dev.mayutama.project.storyappsubm.util.enableScreenAction
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val launcherActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == StoryAddActivity.STORY_ADD_RESULT) {
            navController.popBackStack(R.id.nav_story, false)
            supportFragmentManager.setFragmentResult(
                "request_key",
                bundleOf("refresh_story" to true)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupView() {
        setSupportActionBar(binding.topBar)

        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.nav_story) {
                binding.topBar.setNavigationIcon(R.drawable.outline_arrow_back_24)
                binding.topBar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            } else {
                binding.topBar.navigationIcon = null
            }
        }
    }

    private fun setupAction() {
        onAddStory()
    }

    fun onAddStory() {
        binding.fabAdd.setOnClickListener {
            val optionCompat = ActivityOptionsCompat.makeCustomAnimation(
                this@MainActivity,
                R.anim.slide_in_top,
                R.anim.slide_out_bottom
            )

            val intent = Intent(this@MainActivity, StoryAddActivity::class.java)
            launcherActivityForResult.launch(intent, optionCompat)
        }
    }

    fun showLoading(){
        binding.loadingLayout.root.visibility = View.VISIBLE
        disableScreenAction(window)
    }

    fun hideLoading(){
        binding.loadingLayout.root.visibility = View.GONE
        enableScreenAction(window)
    }
}