import model.RssItem
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest

import org.junit.jupiter.api.Assertions.*

class MainKtTest {

    @Test
    fun xmlParser() {
        // Given
        val link = "https://techblog.woowahan.com/feed"
        // When
        val items = xmlParser(link)
        // Then
        assertEquals(10, items.length)
    }

    @Test
    fun asyncGetRssItemFromLink(): Unit = runTest {
        // Given
        val link = "https://techblog.woowahan.com/feed"
        // When
        val items = asyncGetRssItemFromLink(link)
        // Then
        assertEquals(10, items.size)
    }

    @Test
    fun updateList() {
        // Given
        val originalList = mutableListOf<RssItem>()
        val newList = listOf(
            RssItem("title1", "description1", "Thu, 01 Jan 1970 00:00:00 GMT"),
            RssItem("title2", "description2", "Thu, 01 Jan 1970 00:00:00 GMT"),
            RssItem("title3", "description3", "Thu, 01 Jan 1970 00:00:00 GMT"),
        )
        // When
        updateList(originalList, newList)
        // Then
        assertEquals(3, originalList.size)
    }
}