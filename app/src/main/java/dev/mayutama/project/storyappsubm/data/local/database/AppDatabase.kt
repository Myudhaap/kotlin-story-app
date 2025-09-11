package dev.mayutama.project.storyappsubm.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.mayutama.project.storyappsubm.data.local.dao.RemoteKeysDao
import dev.mayutama.project.storyappsubm.data.local.dao.StoryDao
import dev.mayutama.project.storyappsubm.data.local.entity.RemoteKeys
import dev.mayutama.project.storyappsubm.data.local.entity.StoryEntity
import dev.mayutama.project.storyappsubm.util.Converters

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_notes"
                ).fallbackToDestructiveMigration(true).build()

                return INSTANCE as AppDatabase
            }
        }
    }
}