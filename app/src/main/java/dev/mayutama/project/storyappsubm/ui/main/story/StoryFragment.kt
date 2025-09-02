package dev.mayutama.project.storyappsubm.ui.main.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mayutama.project.storyappsubm.data.remote.dto.res.ErrorRes
import dev.mayutama.project.storyappsubm.databinding.FragmentStoryBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.ui.main.MainActivity
import dev.mayutama.project.storyappsubm.util.ResultState
import dev.mayutama.project.storyappsubm.util.showToast

class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var view: MainActivity
    private lateinit var adapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        view = requireActivity() as MainActivity

        adapter = StoryAdapter(view)
        val layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvStory.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }

        binding.swpRefresh.setOnRefreshListener {
            viewModel.getStories()
            binding.swpRefresh.isRefreshing = false
        }

        observeStories()
    }

    fun observeStories() {
        viewModel.stories.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        view.showLoading()
                    }
                    is ResultState.Success -> {
                        adapter.submitList(result.data.listStory)
                        view.hideLoading()
                    }
                    is ResultState.Error<*> -> {
                        showToast(requireContext(), (result.error as ErrorRes).message)
                        view.hideLoading()
                    }
                }
            }
        }
    }
}