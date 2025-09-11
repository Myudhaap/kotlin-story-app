package dev.mayutama.project.storyappsubm.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.mayutama.project.storyappsubm.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<StoryEntity>)

    @Query("SELECT * FROM mst_story")
    fun getAllQuote(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM mst_story")
    suspend fun deleteAll()
}