import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest
import model.RssItem
import model.RssItems
import model.RssService
import model.RssXmlUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class RssXmlUtilTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Test
    fun rssItemParser() = runTest(testScheduler) {

        launch() {
            // Given
            val link = "https://techblog.woowahan.com/feed"
            // When
            val rssUtil = RssXmlUtil(testDispatcher)
            val items = async { rssUtil.rssItemParser(link) }
            // Then
            assertEquals(10, items.await().length)
        }
    }
}