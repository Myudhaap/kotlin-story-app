package dev.mayutama.project.storyappsubm.ui.main.story

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mayutama.project.storyappsubm.data.remote.dto.res.StoryRes
import dev.mayutama.project.storyappsubm.databinding.ItemStoryBinding
import dev.mayutama.project.storyappsubm.ui.main.MainActivity
import dev.mayutama.project.storyappsubm.ui.storyDetail.StoryDetailActivity

class StoryAdapter(private val activity: MainActivity): ListAdapter<StoryRes.Story, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindingStory(item: StoryRes.Story) {
            binding.tvName.text = item.name
            binding.tvDescription.text = item.description
            Glide.with(binding.root.context)
                .load(item.photoUrl)
                .into(binding.imgStory)

            binding.root.setOnClickListener {
                val optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    Pair(binding.imgStory, "imgStory"),
                    Pair(binding.tvName, "name"),
                    Pair(binding.tvDescription, "description")
                )
                val intent = Intent(binding.root.context, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.STORY_EXTRA, item)
                binding.root.context.startActivity(intent, optionCompat.toBundle())
            }
        }
        fun animateItem() {
            val animatorAlpha = ObjectAnimator.ofFloat(binding.root, View.ALPHA, 0f, 1f)
            val animatorTranslation = ObjectAnimator.ofFloat(binding.root, View.TRANSLATION_X, 100f, 0f)

            animatorAlpha.duration = 600
            animatorTranslation.duration = 600

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animatorAlpha, animatorTranslation)
            animatorSet.start()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStoryBinding.inflate(inflater, parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        if (holder is StoryViewHolder) {
            holder.bindingStory(item)
            holder.animateItem()
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<StoryRes.Story>(){
            override fun areItemsTheSame(
                oldItem: StoryRes.Story,
                newItem: StoryRes.Story
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StoryRes.Story,
                newItem: StoryRes.Story
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}