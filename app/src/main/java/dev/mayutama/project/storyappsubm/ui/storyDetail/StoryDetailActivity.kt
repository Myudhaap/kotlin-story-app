package dev.mayutama.project.storyappsubm.ui.storyDetail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import dev.mayutama.project.storyappsubm.databinding.ActivityStoryDetailBinding

@Suppress("DEPRECATION")
class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupView() {
        setSupportActionBar(binding.topBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "Story Detail"
        }

        val story = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(STORY_EXTRA, StoryRes.Story::class.java)
        else
            intent.getParcelableExtra(STORY_EXTRA)

        if (story != null) {
            with(binding) {
                tvName.text = story.name
                tvDescription.text = story.description
                Glide.with(this@StoryDetailActivity)
                    .load(story.photoUrl)
                    .into(imgStory)
            }
        }
    }

    companion object {
        const val STORY_EXTRA = "story_extra"
    }
}