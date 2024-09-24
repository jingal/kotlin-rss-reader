import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import model.RssData
import model.RssItem
import model.RssItems
import model.RssService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import view.RssView

class RssServiceTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getItemList() = runTest(testScheduler) {
        launch() {
            // Given
            val scope = CoroutineScope(testDispatcher)
            var items = RssData(RssItems(mutableListOf<RssItem>()), RssView())
            val urlList = listOf<String>("https://techblog.woowahan.com/feed", "https://tech.kakao.com/feed")
            val rssService = RssService(scope, Dispatchers.IO, urlList, items)

            // When
            val job = launch {
                rssService.rssDispatching()
            }

            // 5초 동안 가상 시간 진행
            advanceTimeBy(20000L)

            // 코루틴을 취소하여 무한 루프 종료
            job.cancelAndJoin()

            // Assert that the job is cancelled
            assertTrue(job.isCancelled)
        }
    }
}