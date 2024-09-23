import kotlinx.coroutines.*
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

data class RssItem(
    val title: String, val description: String, val pubDate: String,
)

fun xmlParser(link: String): NodeList {
    val factory = DocumentBuilderFactory.newInstance()
    val xml = factory.newDocumentBuilder().parse(link)
    return xml.getElementsByTagName("item")
}

suspend fun asyncGetRssItemFromLink(link: String): List<RssItem> {
    val rssItems = mutableListOf<RssItem>()
    val items = xmlParser(link)
    for (i in 0 until items.length) {
        val item = items.item(i) as Element

        val title = item.getElementsByTagName("title").item(0).textContent
        val description = item.getElementsByTagName("description").item(0).textContent
        val pubDate = item.getElementsByTagName("pubDate").item(0).textContent

        rssItems.add(RssItem(title, description, pubDate))
    }
    return rssItems
}

fun main() {
    val scope = CoroutineScope(Dispatchers.IO)
    var items = listOf<RssItem>()

    // RSS 데이터를 10분마다 업데이트하는 코루틴
    scope.launch {
        while (true) {
            val woowhaItems = async { asyncGetRssItemFromLink("https://techblog.woowahan.com/feed") }
            val kakaoItems = async { asyncGetRssItemFromLink("https://tech.kakao.com/feed") }
            items = woowhaItems.await() + kakaoItems.await()
            items = items.sortedByDescending { LocalDateTime.parse(it.pubDate, DateTimeFormatter.RFC_1123_DATE_TIME) }

            println("RSS 데이터가 업데이트되었습니다.")
            delay( 10 * 1000L) // 10분 (밀리초 단위)
        }
    }

    // 사용자 입력에 따른 검색 기능
    while (true) {
        print("검색어를 입력하세요: ")
        val searchWord = readln().trim().toString()
        items.filter { it.title.contains(searchWord) }.forEach {
            println(it.pubDate)
            println(it.title)
            println(it.description)
            println()
        }
    }
}
