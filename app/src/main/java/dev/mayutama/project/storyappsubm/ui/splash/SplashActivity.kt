package dev.mayutama.project.storyappsubm.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.mayutama.project.storyappsubm.BuildConfig
import dev.mayutama.project.storyappsubm.databinding.ActivitySplashBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.ui.login.LoginActivity
import dev.mayutama.project.storyappsubm.ui.main.MainActivity
import dev.mayutama.project.storyappsubm.util.TokenCache
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.tvVersion.text = "Version ${BuildConfig.VERSION_NAME}"
        playAnimation()

        lifecycleScope.launch {
            delay(3000)
            viewModel.getAuthInfo().collect { auth ->
                val intent = if (auth.token != null) {
                    TokenCache.token = auth.token
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val img = ObjectAnimator.ofFloat(binding.imgApp, View.ALPHA, 1f).setDuration(600)
        val title = ObjectAnimator.ofFloat(binding.tvTitleApp, View.ALPHA, 1f).setDuration(600)
        val version = ObjectAnimator.ofFloat(binding.tvVersion, View.ALPHA, 1f).setDuration(600)

        AnimatorSet().apply {
            play(img).before(
                title
            )
            playTogether(title, version)
            start()
        }
    }
}