package dev.mayutama.project.storyappsubm.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.databinding.ActivityRegisterBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.ui.login.LoginActivity
import dev.mayutama.project.storyappsubm.util.ResultState
import dev.mayutama.project.storyappsubm.util.disableScreenAction
import dev.mayutama.project.storyappsubm.util.enableScreenAction
import dev.mayutama.project.storyappsubm.util.showToast

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupView()
        setupAction()
    }

    private fun setupView(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textView = binding.tvLogin

        val text = textView.text
        val spannable = SpannableString(text)

        val start = text.indexOf("Click here to login")
        val end = start + "Click here to login".length

        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                openLoginActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = Color.BLUE
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannable
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
    }

    private fun setupAction(){
        onRegister()
    }

    private fun onRegister() {
        binding.btnRegister.setOnClickListener {
            val edtName = binding.edtName
            val edtEmail = binding.edtEmail
            val edtPassword = binding.edtPassword

            if (
                edtName.error == null
                && edtEmail.error == null
                && edtPassword.error == null
            ) {
                viewModel.register(
                    edtName.text.toString(),
                    edtEmail.text.toString(),
                    edtPassword.text.toString()
                )
                    .observe(this@RegisterActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading()
                                }
                                is ResultState.Success -> {
                                    hideLoading()
                                    showToast(this@RegisterActivity, result.data.message)
                                    openLoginActivity()
                                }
                                is ResultState.Error<*> -> {
                                    showToast(this@RegisterActivity, (result.error as ErrorRes).message)
                                    hideLoading()
                                }
                            }
                        }
                    }
            } else {
                showToast(this@RegisterActivity, getString(R.string.form_validate_message))
            }
        }
    }

    private fun playAnimation() {
        val tvName = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(300)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val edtName = ObjectAnimator.ofFloat(binding.edtName, View.ALPHA, 1f).setDuration(300)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(300)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(300)
        val btnSignUp = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(300)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(tvName, edtName, tvEmail, edtEmail, tvPassword, edtPassword, btnSignUp, tvLogin)
            start()
        }
    }

    fun openLoginActivity() {
        val optionCompat = ActivityOptionsCompat.makeCustomAnimation(
            this@RegisterActivity,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent, optionCompat.toBundle())
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