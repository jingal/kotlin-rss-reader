import kotlinx.coroutines.*
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory


data class RssItem(
    val title: String, val description: String, val pubDate: String,
)

fun asyncXmlParser(link: String): NodeList {
    val factory = DocumentBuilderFactory.newInstance()
    val xml = factory.newDocumentBuilder().parse(link)

    return xml.getElementsByTagName("item")
}

suspend fun getRssItemFromLink(link: String): List<RssItem> {
    val rssItems = mutableListOf<RssItem>()
    val items = asyncXmlParser(link)
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
    val scope = CoroutineScope(Dispatchers.Default)
    var items = listOf<RssItem>()
    scope.launch {
        val woowhaItems = async { getRssItemFromLink("https://techblog.woowahan.com/feed") }
        val kakaoItems = async { getRssItemFromLink("https://tech.kakao.com/feed") }
        items = woowhaItems.await() + kakaoItems.await()
        items.sortedByDescending { LocalDateTime.parse(it.pubDate, DateTimeFormatter.RFC_1123_DATE_TIME) }
    }
    print("검색어를 입력하세요: ")
    val searchWord = readln().trim().toString()
    items.filter { it.title.contains(searchWord) }.forEach {
        println(it.pubDate)
        println(it.title)
        println(it.description)
        println()
    }
}


