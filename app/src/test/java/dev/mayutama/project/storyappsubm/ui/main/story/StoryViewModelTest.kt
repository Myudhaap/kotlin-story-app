package dev.mayutama.project.storyappsubm.ui.main.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import dev.mayutama.project.storyappsubm.data.local.entity.StoryEntity
import dev.mayutama.project.storyappsubm.data.repository.StoryRepository
import dev.mayutama.project.storyappsubm.dummy.StoryDummy
import dev.mayutama.project.storyappsubm.utils.MainDispatcherRule
import dev.mayutama.project.storyappsubm.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = StoryDummy.generateDummyResponse()
        val data: PagingData<StoryEntity> = QuotePagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStoriesPaging()).thenReturn(expectedStory)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStory: PagingData<StoryEntity> = storyViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryEntity> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStoriesPaging()).thenReturn(expectedStory)

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStory: PagingData<StoryEntity> = storyViewModel.getStories().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }

    class QuotePagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
        companion object {
            fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}