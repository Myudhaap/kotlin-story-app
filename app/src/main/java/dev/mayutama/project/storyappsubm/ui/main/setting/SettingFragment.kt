package dev.mayutama.project.storyappsubm.ui.main.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.databinding.FragmentSettingBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.ui.login.LoginActivity

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupAction() {
        onSettingLanguage()
        onLogout()
    }

    fun onSettingLanguage() {
        binding.language.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
    }

    fun onLogout() {
        binding.logout.setOnClickListener {
            viewModel.logout()
            val optionCompat = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(),
                R.anim.slide_in_top,
                R.anim.slide_out_bottom
            )
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            binding.root.context.startActivity(intent, optionCompat.toBundle())
        }
    }
}