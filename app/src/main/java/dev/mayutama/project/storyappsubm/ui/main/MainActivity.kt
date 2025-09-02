package dev.mayutama.project.storyappsubm.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.databinding.ActivityMainBinding
import dev.mayutama.project.storyappsubm.util.disableScreenAction
import dev.mayutama.project.storyappsubm.util.enableScreenAction
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
            Timber.d("Add story")
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