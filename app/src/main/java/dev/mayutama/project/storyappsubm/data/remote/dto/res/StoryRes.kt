package dev.mayutama.project.storyappsubm.data.remote.dto.res

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

data class StoryRes(
    val error: Boolean,
    val message: Boolean,
    val listStory: List<Story>
) {
    @Parcelize
    data class Story(
        val id: String,
        val name: String,
        val description: String,
        val photoUrl: String,
        val createdAt: Date,
        val lat: Double,
        val lon: Double
    ): Parcelable
}