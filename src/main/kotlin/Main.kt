import model.RssItem
import kotlinx.coroutines.*
import model.RssItems
import model.RssService
import model.RssXmlUtil
import org.w3c.dom.Element
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.plus

suspend fun asyncGetRssItemFromLink(link: String): RssItems  {
    val rssItems = RssItems(mutableListOf<RssItem>())
//    val items = xmlParser(link)
    val items = RssXmlUtil(Dispatchers.IO).rssItemParser(link)

    for (i in 0 until items.length) {
        val item = items.item(i) as Element

        val title = item.getElementsByTagName("title").item(0).textContent
        val description = item.getElementsByTagName("description").item(0).textContent
        val pubDate = item.getElementsByTagName("pubDate").item(0).textContent

        rssItems.add(RssItem(title, description, pubDate))
    }
    return rssItems
}

fun updateList(originalList : MutableList<RssItem>, newList : List<RssItem>) {
    var prevSize = originalList.size

    for( item in newList) {
        if (!originalList.contains(item)) {
                originalList.add(item)
            }
    }

    if (prevSize < originalList.size) {
        println("RSS 데이터가 업데이트되었습니다.")
        originalList.sortByDescending { LocalDateTime.parse(it.pubDate, DateTimeFormatter.RFC_1123_DATE_TIME) }
    }
}

fun main() {
    val scope = CoroutineScope(Dispatchers.IO)
    var items = RssItems(mutableListOf<RssItem>())
    val urlList = listOf<String>("https://techblog.woowahan.com/feed", "https://tech.kakao.com/feed")

    val rssService = RssService(scope, Dispatchers.IO, urlList, items)
    rssService.rssDispatching()

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