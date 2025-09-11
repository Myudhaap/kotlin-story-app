package dev.mayutama.project.storyappsubm.dummy

import dev.mayutama.project.storyappsubm.data.local.entity.StoryEntity
import java.util.Date

object StoryDummy {
    fun generateDummyResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "name $i",
                "description $i",
                "https://story-api.dicoding.dev/images/stories/photos-1757607303389_efa9d05175350c1854a7.jpg",
                Date(),
                1.66,
                1.66,
            )
            items.add(story)
        }
        return items
    }
}