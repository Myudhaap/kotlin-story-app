package dev.mayutama.project.storyappsubm.ui.main.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mayutama.project.storyappsubm.R
import dev.mayutama.project.storyappsubm.databinding.FragmentStoryBinding
import dev.mayutama.project.storyappsubm.factory.ViewModelFactory
import dev.mayutama.project.storyappsubm.ui.main.MainActivity
import dev.mayutama.project.storyappsubm.ui.map.MapsActivity

class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var view: MainActivity
    private lateinit var adapter: StoryAdapter
    private var shouldScrollTop = false

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
        val menuHost: MenuHost = requireActivity()

        adapter = StoryAdapter(view)
        val layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvStory.let {
            it.adapter = adapter.withLoadStateFooter(
                footer = StoryLoadingStateAdapter {
                    adapter.retry()
                }
            )
            it.layoutManager = layoutManager
        }

        binding.swpRefresh.setOnRefreshListener {
            adapter.refresh()
            binding.swpRefresh.isRefreshing = false
        }

        requireActivity().supportFragmentManager.setFragmentResultListener("request_key", viewLifecycleOwner) { _, bundle ->
            val result = bundle.getBoolean("refresh_story", false)
            if (result){
                adapter.refresh()
                shouldScrollTop = true
            }
        }
        observeStories()

        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.story_option, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.list_by_location -> {
                        openMapActivity()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        adapter.addOnPagesUpdatedListener {
            if (shouldScrollTop && adapter.itemCount > 0) {
                (binding.rvStory.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(0, 0)
                shouldScrollTop = false
            }
        }
    }

    fun observeStories() {
        viewModel.getStories().observe(viewLifecycleOwner) { result ->
            adapter.submitData(lifecycle, result)
        }
    }

    fun openMapActivity() {
        val optionCompat = ActivityOptionsCompat.makeCustomAnimation(
            requireContext(),
            R.anim.slide_in_top,
            R.anim.slide_out_bottom
        )

        val intent = Intent(requireContext(), MapsActivity::class.java)
        startActivity(intent, optionCompat.toBundle())
    }
}