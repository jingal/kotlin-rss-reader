import model.RssItem
import kotlinx.coroutines.*
import model.RssData
import model.RssItems
import model.RssService
import model.RssXmlUtil
import org.w3c.dom.Element
import view.RssView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.plus

fun main() {
    val scope = CoroutineScope(Dispatchers.IO)
    var rssData = RssData(RssItems(mutableListOf<RssItem>()), RssView())
    val urlList = listOf<String>("https://techblog.woowahan.com/feed", "https://tech.kakao.com/feed")

    val rssService = RssService(scope, Dispatchers.IO, urlList, rssData)
    rssService.rssDispatching()

    // 사용자 입력에 따른 검색 기능
    while (true) {
        print("검색어를 입력하세요: ")
        val searchWord = readln().trim().toString()
        rssData.search(searchWord)
    }
}